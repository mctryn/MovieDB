package com.mctryn.moviedb.presentation.list.pages

import androidx.compose.ui.test.SemanticsNodeInteractionsProvider
import androidx.compose.ui.test.assertCountEquals
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.hasClickAction
import androidx.compose.ui.test.hasContentDescription
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.onAllNodesWithContentDescription
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performScrollToNode

class MovieListPageObject(
    private val provider: SemanticsNodeInteractionsProvider
) {

    fun verifyLoading() {
        provider.onNodeWithText("Loading movies…").assertIsDisplayed()
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

    fun verifyEmptyState() {
        provider.onNodeWithText("No movies found").assertIsDisplayed()
    }

    fun verifyTitle(title: String) {
        provider.onNodeWithText(title).assertIsDisplayed()
    }

    fun verifyMovieTitle(title: String) {
        provider.onNodeWithText(title).assertIsDisplayed()
    }

    fun verifyMovieRating(movieTitle: String, rating: Double) {
        provider.onNodeWithText(movieTitle).assertIsDisplayed()
        provider.onNodeWithText("${rating}").assertIsDisplayed()
    }

    fun verifyMoviesCount(expectedCount: Int) {
        provider.onAllNodesWithContentDescription("favorite", substring = true)
            .assertCountEquals(expectedCount)
    }

    fun scrollToMovie(title: String) {
        provider
            .onNode(hasClickAction())
            .performScrollToNode(hasText(title))
    }

    fun clickMovie(title: String) {
        provider.onNodeWithText(title).performClick()
    }

    fun clickRefresh() {
        provider.onNodeWithContentDescription("Refresh movies").performClick()
    }

    fun clickToFavorite(movieId: Int) {
        provider.onNodeWithContentDescription("Toggle favorite $movieId").performClick()
    }

    fun clickToUnfavorite(movieId: Int) {
        provider.onNodeWithContentDescription("Untoggle favorite $movieId").performClick()
    }

    fun verifyFavoriteIsAdd(movieId: Int) {
        provider.onNode(hasContentDescription("Toggle favorite $movieId")).assertIsDisplayed()
    }

    fun verifyFavoriteIsRemove(movieId: Int) {
        provider.onNode(hasContentDescription("Untoggle favorite $movieId")).assertIsDisplayed()
    }
}