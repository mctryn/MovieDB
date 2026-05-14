package com.mctryn.moviedb.presentation.details

import androidx.activity.ComponentActivity
import androidx.compose.ui.test.junit4.v2.createAndroidComposeRule
import com.mctryn.moviedb.di.MockRepository
import com.mctryn.moviedb.di.testModule
import com.mctryn.moviedb.domain.model.Movie
import com.mctryn.moviedb.domain.repository.MovieRepository
import com.mctryn.moviedb.presentation.details.pages.MovieDetailsPageObject
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
class MovieDetailsIntegrationTest {

    @get:Rule
    val koinTestRule = KoinTestRule.create {
        modules(testModule)
    }

    @get:Rule
    val composeTestRule = createAndroidComposeRule<ComponentActivity>()

    private lateinit var page: MovieDetailsPageObject
    private lateinit var testRepository: MockRepository

    @OptIn(ExperimentalCoroutinesApi::class)
    @Before
    fun setup() {
        page = MovieDetailsPageObject(composeTestRule)
        testRepository = koinTestRule.koin.get<MovieRepository>() as MockRepository
        testRepository.emitMovieDetailsLoading(movieId = 1)
    }

    @After
    fun teardown() {
        stopKoin()
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun openDetails_showsMovieStory() = runTest {
        composeTestRule.setContent {
            MovieDetailsScreen(movieId = 1)
        }

        page.verifyLoading()

        testRepository.emitMovieDetails(testMovie)
        composeTestRule.waitForIdle()

        page.verifyTitle("Titanic")
        page.verifyOverview("A love story on the famous ship")
        page.verifyReleaseDate("1997-12-19")
        page.verifyRating("7.9")
        page.verifyPoster()
        page.verifyBackButton()
        page.verifyFavoriteControlAdd(movieId = 1)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun favoriteFromDetails_persistsAcrossReload() = runTest {
        composeTestRule.setContent {
            MovieDetailsScreen(movieId = 1)
        }

        testRepository.emitMovieDetails(testMovie)
        composeTestRule.waitForIdle()

        page.verifyFavoriteControlAdd(movieId = 1)

        testRepository.resetToggleFavoriteCalls()
        page.clickToFavorite(movieId = 1)
        testRepository.assertToggleFavoriteCalled(1)

        val favoritedMovie = testMovie.copy(isFavorite = true)
        testRepository.emitMovieDetails(favoritedMovie)
        composeTestRule.waitForIdle()
        page.verifyFavoriteControlRemove(movieId = 1)

        testRepository.resetToggleFavoriteCalls()
        page.clickToUnfavorite(movieId = 1)
        testRepository.assertToggleFavoriteCalled(1)

        testRepository.emitMovieDetails(testMovie.copy(isFavorite = false))
        composeTestRule.waitForIdle()
        page.verifyFavoriteControlAdd(movieId = 1)

        composeTestRule.activityRule.scenario.recreate()
        composeTestRule.setContent {
            MovieDetailsScreen(movieId = 1)
        }
        composeTestRule.waitForIdle()

        page.verifyTitle("Titanic")
        page.verifyFavoriteControlAdd(movieId = 1)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun detailsLoadFailure_retryRecoversToContent() = runTest {
        composeTestRule.setContent {
            MovieDetailsScreen(movieId = 1)
        }

        page.verifyLoading()

        testRepository.emitMovieDetailsError(movieId = 1, message = "Service unavailable")
        composeTestRule.waitForIdle()

        page.verifyError("Service unavailable")
        page.verifyRetryButton()

        testRepository.setRefreshMovieDetailsResult(success = true)
        page.clickRetry()
        testRepository.assertRefreshMovieDetailsCalled(1)

        testRepository.emitMovieDetailsLoading(movieId = 1)
        testRepository.emitMovieDetails(testMovie)
        composeTestRule.waitForIdle()

        page.verifyTitle("Titanic")
        page.verifyOverview("A love story on the famous ship")
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun detailsRecreate_keepsSameMovieDisplayed() = runTest {
        composeTestRule.setContent {
            MovieDetailsScreen(movieId = 1)
        }

        testRepository.emitMovieDetails(testMovie)
        composeTestRule.waitForIdle()

        page.verifyTitle("Titanic")
        page.verifyOverview("A love story on the famous ship")

        composeTestRule.activityRule.scenario.recreate()
        composeTestRule.setContent {
            MovieDetailsScreen(movieId = 1)
        }
        composeTestRule.waitForIdle()

        page.verifyTitle("Titanic")
        page.verifyOverview("A love story on the famous ship")
        page.verifyReleaseDate("1997-12-19")
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun invalidMovieId_showsErrorState() = runTest {
        composeTestRule.setContent {
            MovieDetailsScreen(movieId = -1)
        }

        composeTestRule.waitForIdle()

        page.verifyError("Invalid movie id")
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun detailsRecreate_keepsMovieIdFromScreenArgument() = runTest {
        composeTestRule.setContent {
            MovieDetailsScreen(movieId = 1)
        }

        testRepository.emitMovieDetails(testMovie)
        composeTestRule.waitForIdle()
        page.verifyTitle("Titanic")

        composeTestRule.activityRule.scenario.recreate()
        composeTestRule.setContent {
            MovieDetailsScreen(movieId = 1)
        }
        composeTestRule.waitForIdle()

        page.verifyTitle("Titanic")
        testRepository.assertRefreshMovieDetailsCalled(1)
    }
}

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
