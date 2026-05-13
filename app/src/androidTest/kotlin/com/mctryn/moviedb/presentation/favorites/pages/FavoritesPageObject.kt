package com.mctryn.moviedb.presentation.favorites.pages

import androidx.compose.ui.test.SemanticsNodeInteractionsProvider
import androidx.compose.ui.test.assertCountEquals
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.hasContentDescription
import androidx.compose.ui.test.onAllNodesWithContentDescription
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick

class FavoritesPageObject(
    private val provider: SemanticsNodeInteractionsProvider
) {
    fun verifyTitle(title: String) {
        provider.onNodeWithText(title).assertIsDisplayed()
    }

    fun verifyLoading() {
        provider.onNodeWithText("Loading favorites…").assertIsDisplayed()
    }

    fun verifyEmptyState() {
        provider.onNodeWithText("No favorite movies yet").assertIsDisplayed()
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

    fun verifyMovieTitle(title: String) {
        provider.onNodeWithText(title).assertIsDisplayed()
    }

    fun verifyMoviesCount(expectedCount: Int) {
        provider.onAllNodesWithContentDescription("favorite", substring = true)
            .assertCountEquals(expectedCount)
    }

    fun clickMovie(title: String) {
        provider.onNodeWithText(title).performClick()
    }

    fun clickToUnfavorite(movieId: Int) {
        provider.onNodeWithContentDescription("Untoggle favorite $movieId").performClick()
    }

    fun verifyFavoriteControlRemove(movieId: Int) {
        provider.onNode(hasContentDescription("Untoggle favorite $movieId")).assertIsDisplayed()
    }

    fun verifyMovieNotDisplayed(title: String) {
        provider.onNodeWithText(title).assertDoesNotExist()
    }
}
