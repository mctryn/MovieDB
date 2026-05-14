package com.mctryn.moviedb.navigation

import androidx.activity.ComponentActivity
import androidx.compose.ui.test.junit4.v2.createAndroidComposeRule
import androidx.navigation3.runtime.entryProvider
import com.mctryn.moviedb.di.MockRepository
import com.mctryn.moviedb.di.navigationTestModule
import com.mctryn.moviedb.navigation.pages.NavigationPageObject
import com.mctryn.moviedb.domain.model.Movie
import com.mctryn.moviedb.domain.repository.MovieRepository
import com.mctryn.moviedb.presentation.favorites.FavoritesScreen
import com.mctryn.moviedb.presentation.list.MovieListScreen
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.koin.core.context.stopKoin
import org.koin.dsl.module
import org.koin.test.KoinTestRule

@RunWith(JUnit4::class)
class NavigationIntegrationTest {

    @get:Rule
    val koinTestRule = KoinTestRule.create {
        modules(
            navigationTestModule,
            module {
                single<AppEntryProvider> {
                    entryProvider {
                        entry<MovieListNav> {
                            val navigationManager: NavigationManager = get()
                            MovieListScreen(navigateToFavoriteList = { navigationManager.navigateToFavorites() })
                        }
                        entry<FavoritesNav> {
                            val navigationManager: NavigationManager = get()
                            FavoritesScreen(navigateToMoviesList = { navigationManager.navigateToMovieList() })
                        }
                        entry<MovieDetailsNav> { key ->
                            com.mctryn.moviedb.presentation.details.MovieDetailsScreen(movieId = key.movieId)
                        }
                    }
                }
            }
        )
    }

    @get:Rule
    val composeTestRule = createAndroidComposeRule<ComponentActivity>()

    private lateinit var page: NavigationPageObject
    private lateinit var testRepository: MockRepository

    @Before
    fun setup() {
        page = NavigationPageObject(composeTestRule)
        testRepository = koinTestRule.koin.get<MovieRepository>() as MockRepository
        testRepository.emitPopularMoviesLoading()
        testRepository.emitFavoritesLoading()
    }

    @After
    fun teardown() {
        stopKoin()
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun bottomBar_switchesBetweenMovieListAndFavorites() = runTest {
        composeTestRule.setContent {
            MovieDbApp()
        }

        testRepository.emitPopularMovies(testMovies)
        composeTestRule.waitForIdle()

        page.verifyMovieListVisible()
        page.clickBottomFavorites()

        testRepository.emitFavorites(testFavoriteMovies)
        composeTestRule.waitForIdle()

        page.verifyFavoritesVisible()
        page.clickBottomMovies()
        composeTestRule.waitForIdle()
        page.verifyMovieListVisible()
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun fromMovieList_openDetails_backReturnsToList() = runTest {
        composeTestRule.setContent {
            MovieDbApp()
        }

        testRepository.emitPopularMovies(testMovies)
        composeTestRule.waitForIdle()

        page.waitForMovie("Titanic")
        page.clickMovie("Titanic")
        testRepository.emitMovieDetails(testMovie)
        composeTestRule.waitForIdle()

        page.verifyDetailsVisible("Titanic")

        composeTestRule.activity.onBackPressedDispatcher.onBackPressed()
        composeTestRule.waitForIdle()

        page.verifyMovieListVisible()

        page.waitForMovie("Inception")
        page.clickMovie("Inception")
        testRepository.emitMovieDetails(testMovies[1])
        composeTestRule.waitForIdle()

        page.verifyDetailsVisible("Inception")

        composeTestRule.activity.onBackPressedDispatcher.onBackPressed()
        composeTestRule.waitForIdle()

        page.verifyMovieListVisible()
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun fromFavorites_openDetails_backReturnsToFavorites() = runTest {
        composeTestRule.setContent {
            MovieDbApp()
        }

        page.clickBottomFavorites()
        testRepository.emitFavorites(testFavoriteMovies)
        composeTestRule.waitForIdle()

        page.waitForMovie("Inception")
        page.clickMovie("Inception")
        testRepository.emitMovieDetails(testFavoriteMovies[1])
        composeTestRule.waitForIdle()

        page.verifyDetailsVisible("Inception")

        composeTestRule.activity.onBackPressedDispatcher.onBackPressed()
        composeTestRule.waitForIdle()

        page.verifyFavoritesVisible()
        page.waitForMovie("Inception")
    }
}

private val testMovies = listOf(
    Movie(1, "Titanic", "A love story on the famous ship", null, "1997-12-19", 7.9, 22000, false),
    Movie(2, "Inception", "A dream within a dream", null, "2010-07-16", 8.4, 32000, false),
    Movie(3, "The Matrix", "What is real?", null, "1999-03-31", 8.2, 25000, false)
)

private val testFavoriteMovies = listOf(
    Movie(1, "Titanic", "A love story on the famous ship", null, "1997-12-19", 7.9, 22000, true),
    Movie(2, "Inception", "A dream within a dream", null, "2010-07-16", 8.4, 32000, true)
)

private val testMovie = Movie(
    id = 1,
    title = "Titanic",
    overview = "A love story on the famous ship",
    posterPath = null,
    releaseDate = "1997-12-19",
    voteAverage = 7.9,
    voteCount = 22000,
    isFavorite = false
)
