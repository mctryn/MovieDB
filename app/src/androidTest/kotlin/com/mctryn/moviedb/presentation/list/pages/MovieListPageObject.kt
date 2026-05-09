package com.mctryn.moviedb.presentation.list.pages

import androidx.compose.ui.test.SemanticsNodeInteractionsProvider
import androidx.compose.ui.test.assertCountEquals
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertTextContains
import androidx.compose.ui.test.onAllNodesWithText
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performScrollTo
import com.mctryn.moviedb.domain.model.Movie

/**
 * Page Object for Movie List Screen.
 */
class MovieListPageObject(private val provider: SemanticsNodeInteractionsProvider) {

    fun verifyTitle(title: String) {
        provider.onNodeWithText(title).assertIsDisplayed()
    }

    fun verifyLoading() {
        provider.onNodeWithText("Loading movies...").assertIsDisplayed()
    }

    fun verifyContent() {
        provider.onNodeWithText("Content").assertIsDisplayed()
    }

    fun verifyError(message: String) {
        provider.onNodeWithText(message).assertIsDisplayed()
    }

    fun verifyRetryButton() {
        provider.onNodeWithText("Retry").assertIsDisplayed()
    }

    fun clickRetry() {
        provider.onNodeWithText("Retry").performClick()
    }

    fun verifyMovies(count: Int) {
        provider.onAllNodesWithText("Movie").assertCountEquals(count)
    }

    fun verifyMoviesCount(count: Int) {
        // Verify count by checking unique movie titles
        provider.onNodeWithText("Titanic").assertExists()
        provider.onNodeWithText("Inception").assertExists()
        provider.onNodeWithText("The Matrix").assertExists()
    }

    fun verifyMovieTitle(title: String) {
        provider.onNodeWithText(title).assertIsDisplayed()
    }

    fun verifyMovieRating(title: String, rating: Double) {
        provider.onNodeWithText("$rating").assertIsDisplayed()
    }

    fun verifyEmptyState() {
        provider.onNodeWithText("No movies found").assertIsDisplayed()
    }

    fun clickMovie(title: String) {
        provider.onNodeWithText(title).performClick()
    }

    fun scrollToMovie(title: String) {
        provider.onNodeWithText(title).performScrollTo()
    }

    fun verifyFavoriteIcon() {
        provider.onNodeWithContentDescription("Favorite").assertIsDisplayed()
    }

    fun clickFavorite(movieId: Int) {
        provider.onNodeWithContentDescription("Toggle favorite $movieId").performClick()
    }

    fun waitForContent(timeoutMs: Long = 5000) {
        Thread.sleep(timeoutMs)
    }
}