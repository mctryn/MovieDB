package com.mctryn.moviedb.presentation.list

import androidx.activity.ComponentActivity
import androidx.compose.ui.test.junit4.v2.createAndroidComposeRule
import com.mctryn.moviedb.di.MockRepository
import com.mctryn.moviedb.di.testModule
import com.mctryn.moviedb.domain.model.Movie
import com.mctryn.moviedb.domain.repository.MovieRepository
import com.mctryn.moviedb.navigation.NavigationManager
import com.mctryn.moviedb.navigation.TestNavigationManager
import com.mctryn.moviedb.presentation.list.pages.MovieListPageObject
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.koin.core.context.stopKoin
import org.koin.test.KoinTestRule

@RunWith(JUnit4::class)
class MovieListIntegrationTest {

    @get:Rule
    val koinTestRule = KoinTestRule.create {
        modules(testModule)
    }

    @get:Rule
    val composeTestRule = createAndroidComposeRule<ComponentActivity>()

    private lateinit var page: MovieListPageObject
    private lateinit var testRepository: MockRepository
    private lateinit var testNavManager: TestNavigationManager

    @OptIn(ExperimentalCoroutinesApi::class)
    @Before
    fun setup() {
        page = MovieListPageObject(composeTestRule)
        testRepository = koinTestRule.koin.get<MovieRepository>() as MockRepository
        testNavManager = koinTestRule.koin.get<NavigationManager>() as TestNavigationManager
        testNavManager.reset()
        testRepository.emitPopularMoviesLoading()
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @After
    fun teardown() {
        stopKoin()
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun happyPath_completeUserJourney() = runTest {
        composeTestRule.setContent {
            MovieListScreen()
        }
        page.verifyLoading()
        testRepository.emitPopularMovies(testMovies)
        composeTestRule.waitForIdle()
        page.verifyMovieTitle("Titanic")
        page.verifyMovieTitle("Inception")
        page.verifyMovieTitle("The Matrix")
        page.verifyTitle("Movie Browser")
        page.verifyMoviesCount(3)
        page.verifyMovieRating("Titanic", 7.9)
        page.scrollToMovie("The Matrix")
        page.verifyMovieTitle("The Matrix")
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun navigateFromContentState_navigatesToDetails() = runTest {
        composeTestRule.setContent {
            MovieListScreen()
        }
        testRepository.emitPopularMovies(testMovies)
        composeTestRule.waitForIdle()
        page.clickMovie("Inception")
        testNavManager.assertNavigatedToMovieId(2)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun toggleFavorite_marksAsFavorite() = runTest {
        composeTestRule.setContent {
            MovieListScreen()
        }
        testRepository.emitPopularMovies(testMovies)
        composeTestRule.waitForIdle()
        testRepository.resetToggleFavoriteCalls()
        page.clickFavorite(1)
        testRepository.assertToggleFavoriteCalled(1)
        val updatedMovies = testMovies.map { movie ->
            if (movie.id == 1) movie.copy(isFavorite = true) else movie
        }
        testRepository.emitPopularMovies(updatedMovies)
        composeTestRule.waitForIdle()
    }
}

private val sampleMovies = listOf(
    Movie(1, "Titanic", "A love story on the famous ship", null, "1997-12-19", 7.9, 22000, false),
    Movie(2, "Inception", "A dream within a dream", null, "2010-07-16", 8.4, 32000, false),
    Movie(3, "The Matrix", "What is real?", null, "1999-03-31", 8.2, 25000, false)
)

val testMovies: List<Movie> = sampleMovies