package com.mctryn.moviedb.navigation.pages

import androidx.compose.ui.test.assertCountEquals
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.ComposeTestRule
import androidx.compose.ui.test.onAllNodesWithText
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick

class NavigationPageObject(
    private val provider: ComposeTestRule
) {
    fun clickBottomMovies() {
        provider.onNodeWithText("Movies").performClick()
    }

    fun clickBottomFavorites() {
        provider.onNodeWithText("Favorites").performClick()
    }

    fun verifyMovieListVisible() {
        provider.onNodeWithText("Movie Browser").assertIsDisplayed()
    }

    fun verifyFavoritesVisible() {
        provider.onAllNodesWithText("Favorites")
            .assertCountEquals(2)
    }

    fun waitForMovie(title: String, timeoutMillis: Long = 5_000) {
        provider.waitUntil(timeoutMillis) {
            provider.onAllNodesWithText(title).fetchSemanticsNodes().isNotEmpty()
        }
    }

    fun clickMovie(title: String) {
        provider.onNodeWithText(title).performClick()
    }

    fun verifyDetailsVisible(title: String) {
        provider.onNodeWithText("Movie Details").assertIsDisplayed()
        provider.onNodeWithText(title).assertIsDisplayed()
    }
}
