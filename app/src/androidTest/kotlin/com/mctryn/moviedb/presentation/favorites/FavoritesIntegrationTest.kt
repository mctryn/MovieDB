package com.mctryn.moviedb.presentation.favorites

import androidx.activity.ComponentActivity
import androidx.compose.runtime.Composable
import androidx.compose.ui.test.junit4.v2.createAndroidComposeRule
import com.mctryn.moviedb.di.MockRepository
import com.mctryn.moviedb.di.testModule
import com.mctryn.moviedb.domain.model.Movie
import com.mctryn.moviedb.domain.repository.MovieRepository
import com.mctryn.moviedb.navigation.NavigationManager
import com.mctryn.moviedb.navigation.TestNavigationManager
import com.mctryn.moviedb.presentation.favorites.pages.FavoritesPageObject
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
class FavoritesIntegrationTest {

    @get:Rule
    val koinTestRule = KoinTestRule.create {
        modules(testModule)
    }

    @get:Rule
    val composeTestRule = createAndroidComposeRule<ComponentActivity>()

    private lateinit var page: FavoritesPageObject
    private lateinit var testRepository: MockRepository
    private lateinit var testNavManager: TestNavigationManager

    @OptIn(ExperimentalCoroutinesApi::class)
    @Before
    fun setup() {
        page = FavoritesPageObject(composeTestRule)
        testRepository = koinTestRule.koin.get<MovieRepository>() as MockRepository
        testNavManager = koinTestRule.koin.get<NavigationManager>() as TestNavigationManager
        testNavManager.reset()
        testRepository.emitFavoritesLoading()
    }

    @After
    fun teardown() {
        stopKoin()
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun openFavorites_showsSavedMovies() = runTest {
        composeTestRule.setContent {
            FavoritesScreenStub()
        }

        page.verifyLoading()

        testRepository.emitFavorites(testFavoriteMovies)
        composeTestRule.waitForIdle()

        page.verifyTitle("Favorites")
        page.verifyMovieTitle("Titanic")
        page.verifyMovieTitle("Inception")
        page.verifyMoviesCount(2)
        page.verifyFavoriteControlRemove(1)
        page.verifyFavoriteControlRemove(2)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun openFavorites_showsEmptyStateWhenNoFavorites() = runTest {
        composeTestRule.setContent {
            FavoritesScreenStub()
        }

        page.verifyLoading()

        testRepository.emitFavorites(emptyList())
        composeTestRule.waitForIdle()

        page.verifyTitle("Favorites")
        page.verifyEmptyState()
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun clickFavorite_navigatesToMovieDetails() = runTest {
        composeTestRule.setContent {
            FavoritesScreenStub()
        }

        testRepository.emitFavorites(testFavoriteMovies)
        composeTestRule.waitForIdle()

        page.clickMovie("Inception")
        testNavManager.assertNavigatedToMovieId(2)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun unfavoriteFromFavorites_removesMovieFromList() = runTest {
        composeTestRule.setContent {
            FavoritesScreenStub()
        }

        testRepository.emitFavorites(testFavoriteMovies)
        composeTestRule.waitForIdle()

        page.verifyMovieTitle("Titanic")
        page.verifyMovieTitle("Inception")
        page.verifyFavoriteControlRemove(1)

        testRepository.resetToggleFavoriteCalls()
        page.clickToUnfavorite(1)
        testRepository.assertToggleFavoriteCalled(1)

        testRepository.emitFavorites(listOf(testFavoriteMovies[1]))
        composeTestRule.waitForIdle()

        page.verifyMovieNotDisplayed("Titanic")
        page.verifyMovieTitle("Inception")
        page.verifyMoviesCount(1)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun favoritesLoadFailure_retryRecoversToContent() = runTest {
        composeTestRule.setContent {
            FavoritesScreenStub()
        }

        page.verifyLoading()

        testRepository.emitFavoritesError("Service unavailable")
        composeTestRule.waitForIdle()

        page.verifyError("Service unavailable")
        page.verifyRetryButton()

        page.clickRetry()
        testRepository.emitFavorites(testFavoriteMovies)
        composeTestRule.waitForIdle()

        page.verifyMovieTitle("Titanic")
        page.verifyMovieTitle("Inception")
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun favoritesLoadFailure_retryRecoversToEmpty() = runTest {
        composeTestRule.setContent {
            FavoritesScreenStub()
        }

        testRepository.emitFavoritesError("Service unavailable")
        composeTestRule.waitForIdle()

        page.verifyError("Service unavailable")
        page.clickRetry()

        testRepository.emitFavorites(emptyList())
        composeTestRule.waitForIdle()

        page.verifyEmptyState()
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun favoritesRecreate_keepsSameMoviesDisplayed() = runTest {
        composeTestRule.setContent {
            FavoritesScreenStub()
        }

        testRepository.emitFavorites(testFavoriteMovies)
        composeTestRule.waitForIdle()

        page.verifyMovieTitle("Titanic")
        page.verifyMovieTitle("Inception")

        composeTestRule.activityRule.scenario.recreate()
        composeTestRule.setContent {
            FavoritesScreenStub()
        }
        composeTestRule.waitForIdle()

        page.verifyMovieTitle("Titanic")
        page.verifyMovieTitle("Inception")
        page.verifyMoviesCount(2)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun favoritesUpdatesFromRepository_areReflectedOnScreen() = runTest {
        composeTestRule.setContent {
            FavoritesScreenStub()
        }

        testRepository.emitFavorites(listOf(testFavoriteMovies[0]))
        composeTestRule.waitForIdle()
        page.verifyMovieTitle("Titanic")
        page.verifyMovieNotDisplayed("Inception")

        testRepository.emitFavorites(testFavoriteMovies)
        composeTestRule.waitForIdle()
        page.verifyMovieTitle("Titanic")
        page.verifyMovieTitle("Inception")

        testRepository.emitFavorites(listOf(testFavoriteMovies[1]))
        composeTestRule.waitForIdle()
        page.verifyMovieNotDisplayed("Titanic")
        page.verifyMovieTitle("Inception")
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun rapidUnfavoriteClicks_keepsUiStable() = runTest {
        composeTestRule.setContent {
            FavoritesScreenStub()
        }

        testRepository.emitFavorites(testFavoriteMovies)
        composeTestRule.waitForIdle()

        testRepository.resetToggleFavoriteCalls()
        page.clickToUnfavorite(1)
        page.clickToUnfavorite(1)

        testRepository.assertToggleFavoriteCalled(1)

        testRepository.emitFavorites(listOf(testFavoriteMovies[1]))
        composeTestRule.waitForIdle()
        page.verifyMovieTitle("Inception")
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun clickDuringLoadingOrError_doesNotNavigate() = runTest {
        composeTestRule.setContent {
            FavoritesScreenStub()
        }

        page.verifyLoading()
        testNavManager.assertNoNavigation()

        testRepository.emitFavoritesError("Service unavailable")
        composeTestRule.waitForIdle()
        page.verifyError("Service unavailable")
        testNavManager.assertNoNavigation()
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun staleFavoritesUpdate_doesNotRegressVisibleState() = runTest {
        composeTestRule.setContent {
            FavoritesScreenStub()
        }

        testRepository.emitFavorites(listOf(testFavoriteMovies[0]))
        composeTestRule.waitForIdle()
        page.verifyMovieTitle("Titanic")

        testRepository.emitFavorites(testFavoriteMovies)
        composeTestRule.waitForIdle()
        page.verifyMovieTitle("Titanic")
        page.verifyMovieTitle("Inception")
    }
}

@Composable
private fun FavoritesScreenStub() {
    FavoritesScreen()
}

private val testFavoriteMovies = listOf(
    Movie(1, "Titanic", "A love story on the famous ship", null, "1997-12-19", 7.9, 22000, true),
    Movie(2, "Inception", "A dream within a dream", null, "2010-07-16", 8.4, 32000, true)
)
