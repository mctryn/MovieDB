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
    fun userStory_browseRefreshRecoverAndOpenDetails() = runTest {
        composeTestRule.setContent {
            MovieListScreen()
        }

        page.verifyLoading()

        testRepository.emitPopularMovies(testMovies)
        composeTestRule.waitForIdle()

        page.verifyTitle("Movie Browser")
        page.verifyMovieTitle("Titanic")
        page.verifyMovieTitle("Inception")
        page.verifyMoviesCount(3)

        page.clickRefresh()

        testRepository.emitPopularMoviesError("Network unavailable")
        composeTestRule.waitForIdle()

        page.verifyError("Network unavailable")
        page.verifyRetryButton()

        page.clickRetry()

        testRepository.emitPopularMoviesLoading()
        composeTestRule.waitForIdle()
        page.verifyLoading()

        val refreshedMovies = testMovies.map { movie ->
            if (movie.id == 2) movie.copy(voteAverage = 8.5) else movie
        }
        testRepository.emitPopularMovies(refreshedMovies)
        composeTestRule.waitForIdle()

        page.verifyMovieTitle("Inception")
        page.verifyMovieRating("Inception", 8.5)

        page.clickMovie("Inception")
        testNavManager.assertNavigatedToMovieId(2)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun userStory_favoriteLifecycle_fromAddToRemove_persistedByFlow() = runTest {
        composeTestRule.setContent {
            MovieListScreen()
        }

        testRepository.emitPopularMovies(testMovies)
        composeTestRule.waitForIdle()

        page.verifyMovieTitle("Titanic")
        page.verifyFavoriteIsAdd(1)

        testRepository.resetToggleFavoriteCalls()
        page.clickToFavorite(1)
        testRepository.assertToggleFavoriteCalled(1)

        val favoritedMovies = testMovies.map { movie ->
            if (movie.id == 1) movie.copy(isFavorite = true) else movie
        }
        testRepository.emitPopularMovies(favoritedMovies)
        composeTestRule.waitForIdle()

        page.verifyFavoriteIsRemove(1)

        page.clickToUnfavorite(1)
        testRepository.assertToggleFavoriteCalled(1)

        val unfavoritedMovies = favoritedMovies.map { movie ->
            if (movie.id == 1) movie.copy(isFavorite = false) else movie
        }
        testRepository.emitPopularMovies(unfavoritedMovies)
        composeTestRule.waitForIdle()

        page.verifyFavoriteIsAdd(1)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun userStory_errorFirst_thenRetry_toUsableState() = runTest {
        composeTestRule.setContent {
            MovieListScreen()
        }

        page.verifyLoading()

        testRepository.emitPopularMoviesError("Service unavailable")
        composeTestRule.waitForIdle()

        page.verifyError("Service unavailable")
        page.verifyRetryButton()

        page.clickRetry()

        testRepository.emitPopularMovies(testMovies)
        composeTestRule.waitForIdle()

        page.verifyMovieTitle("The Matrix")

        page.clickToFavorite(3)
        testRepository.assertToggleFavoriteCalled(3)

        val updatedMovies = testMovies.map { movie ->
            if (movie.id == 3) movie.copy(isFavorite = true) else movie
        }
        testRepository.emitPopularMovies(updatedMovies)
        composeTestRule.waitForIdle()

        page.verifyFavoriteIsRemove(3)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun userStory_multiAction_session_stays_consistent() = runTest {
        composeTestRule.setContent {
            MovieListScreen()
        }

        testRepository.emitPopularMovies(testMovies)
        composeTestRule.waitForIdle()

        page.verifyMovieTitle("Titanic")
        page.verifyMovieTitle("Inception")
        page.verifyMovieTitle("The Matrix")

        page.clickToFavorite(1)
        page.clickToFavorite(2)

        testRepository.assertToggleFavoriteCalled(1)
        testRepository.assertToggleFavoriteCalled(2)

        val favoritedFirstTwo = testMovies.map { movie ->
            when (movie.id) {
                1, 2 -> movie.copy(isFavorite = true)
                else -> movie
            }
        }
        testRepository.emitPopularMovies(favoritedFirstTwo)
        composeTestRule.waitForIdle()

        page.verifyFavoriteIsRemove(1)
        page.verifyFavoriteIsRemove(2)

        page.clickRefresh()
        testRepository.emitPopularMovies(favoritedFirstTwo)
        composeTestRule.waitForIdle()

        page.verifyFavoriteIsRemove(1)
        page.verifyFavoriteIsRemove(2)

        page.clickMovie("Titanic")
        testNavManager.assertNavigatedToMovieId(1)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun statePreservation_afterRecreate_keepsContentAndFavoriteState() = runTest {
        composeTestRule.setContent {
            MovieListScreen()
        }

        testRepository.emitPopularMovies(testMovies)
        composeTestRule.waitForIdle()
        page.verifyMovieTitle("Titanic")
        page.verifyFavoriteIsAdd(1)

        page.clickToFavorite(1)
        testRepository.assertToggleFavoriteCalled(1)

        val favorited = testMovies.map { movie ->
            if (movie.id == 1) movie.copy(isFavorite = true) else movie
        }
        testRepository.emitPopularMovies(favorited)
        composeTestRule.waitForIdle()
        page.verifyFavoriteIsRemove(1)

        composeTestRule.activityRule.scenario.recreate()
        composeTestRule.setContent {
            MovieListScreen()
        }
        composeTestRule.waitForIdle()

        page.verifyMovieTitle("Titanic")
        page.verifyMovieTitle("Inception")
        page.verifyMovieTitle("The Matrix")
        page.verifyFavoriteIsRemove(1)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun emptyList_showsEmptyMessage() = runTest {
        composeTestRule.setContent {
            MovieListScreen()
        }

        testRepository.emitPopularMovies(emptyList())
        composeTestRule.waitForIdle()

        page.verifyEmptyState()
    }
}

private val testMovies = listOf(
    Movie(1, "Titanic", "A love story on the famous ship", null, "1997-12-19", 7.9, 22000, false),
    Movie(2, "Inception", "A dream within a dream", null, "2010-07-16", 8.4, 32000, false),
    Movie(3, "The Matrix", "What is real?", null, "1999-03-31", 8.2, 25000, false)
)
