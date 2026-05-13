package com.mctryn.moviedb.presentation.details.pages

import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.hasContentDescription
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.junit4.ComposeTestRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick

class MovieDetailsPageObject(
    private val provider: ComposeTestRule
) {
    fun verifyLoading() {
        provider.onNodeWithText("Loading favorites…").assertIsDisplayed()
    }

    @OptIn(ExperimentalTestApi::class)
    fun verifyTitle(title: String) {
        provider.waitUntilAtLeastOneExists(hasText(title), 5_000)
        provider.onNodeWithText(title).assertIsDisplayed()
    }

    @OptIn(ExperimentalTestApi::class)
    fun verifyOverview(overview: String) {
        provider.waitUntilAtLeastOneExists(hasText(overview), 5_000)
        provider.onNodeWithText(overview).assertIsDisplayed()
    }

    @OptIn(ExperimentalTestApi::class)
    fun verifyReleaseDate(releaseDate: String) {
        provider.waitUntilAtLeastOneExists(hasText(releaseDate), 5_000)
        provider.onNodeWithText(releaseDate).assertIsDisplayed()
    }

    @OptIn(ExperimentalTestApi::class)
    fun verifyRating(rating: String) {
        val localizedRating = rating.replace('.', ',')
        val ratingMatcher = hasText(rating).or(hasText(localizedRating))
        provider.waitUntilAtLeastOneExists(ratingMatcher, 5_000)
        provider.onNode(ratingMatcher).assertIsDisplayed()
    }

    fun verifyPoster() {
        provider.onNode(hasContentDescription("Movie poster")).assertIsDisplayed()
    }

    fun verifyBackButton() {
        provider.onNode(hasContentDescription("Back")).assertIsDisplayed()
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

    fun clickToFavorite(movieId: Int) {
        provider.onNode(hasContentDescription("Toggle favorite $movieId")).performClick()
    }

    fun clickToUnfavorite(movieId: Int) {
        provider.onNode(hasContentDescription("Untoggle favorite $movieId")).performClick()
    }

    fun verifyFavoriteControlAdd(movieId: Int) {
        provider.onNode(hasContentDescription("Toggle favorite $movieId")).assertIsDisplayed()
    }

    fun verifyFavoriteControlRemove(movieId: Int) {
        provider.onNode(hasContentDescription("Untoggle favorite $movieId")).assertIsDisplayed()
    }
}
