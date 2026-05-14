package com.mctryn.moviedb.navigation

import androidx.navigation3.runtime.NavKey

/**
 * Test Implementation of [NavigationManager] for UI integration tests.
 * 
 * Provides full control over navigation actions from tests without
 * requiring actual navigation to occur.
 * 
 * ## Usage:
 * 
 * ```kotlin
 * @Before
 * fun setup() {
 *     testNavManager = TestNavigationManager()
 * }
 * 
 * @Test
 * fun navigateToDetails_verifiesCallback() {
 *     // ... setup screen
 *     
 *     page.clickMovie("Inception")
 *     
 *     // Verify navigation was called with correct ID
 *     testNavManager.assertNavigatedToMovieId(2)
 * }
 * ```
 */
class TestNavigationManager : NavigationManager {

    private val _navigatedToMovieIds = mutableListOf<Int>()
    val navigatedToMovieIds: List<Int> = _navigatedToMovieIds

    private val _topLevelNavigations = mutableListOf<NavKey>()
    val topLevelNavigations: List<NavKey> = _topLevelNavigations

    private val _navigatedBack = mutableListOf<Unit>()
    val navigatedBack: List<Unit> = _navigatedBack

    private var _canNavigateBack = false
    val canNavigateBack: Boolean get() = _canNavigateBack

    override fun navigateToMovieDetails(movieId: Int) {
        _navigatedToMovieIds.add(movieId)
    }

    override fun navigateToMovieList() {
        _topLevelNavigations.add(MovieListNav)
    }

    override fun navigateToFavorites() {
        _topLevelNavigations.add(FavoritesNav)
    }

    override fun navigateBack() {
        _navigatedBack.add(Unit)
    }

    override fun canNavigateBack(): Boolean = _canNavigateBack

    /**
     * Reset all navigation state.
     * Call in @Before or @After.
     */
    fun reset() {
        _navigatedToMovieIds.clear()
        _topLevelNavigations.clear()
        _navigatedBack.clear()
        _canNavigateBack = false
    }

    /**
     * Set the value returned by canNavigateBack().
     * @param canNavigateBack true if back navigation is possible
     */
    fun setCanNavigateBack(canNavigateBack: Boolean) {
        _canNavigateBack = canNavigateBack
    }

    /**
     * Assert that navigation to movie details was called exactly once.
     * @param expectedMovieId The expected movie ID
     */
    fun assertNavigatedToMovieId(expectedMovieId: Int) {
        check(_navigatedToMovieIds.size == 1) {
            "Expected exactly 1 navigation to movie details, but got ${_navigatedToMovieIds.size}"
        }
        check(_navigatedToMovieIds[0] == expectedMovieId) {
            "Expected navigation to movie $expectedMovieId, but navigated to ${_navigatedToMovieIds[0]}"
        }
    }

    /**
     * Assert that no navigation was called.
     */
    fun assertNoNavigation() {
        check(_navigatedToMovieIds.isEmpty() && _topLevelNavigations.isEmpty()) {
            "Expected no navigation, but got movieDetails=$_navigatedToMovieIds topLevel=$_topLevelNavigations"
        }
    }

    fun assertNavigatedToMovieList() {
        check(_topLevelNavigations.lastOrNull() == MovieListNav) {
            "Expected last top-level navigation to MovieListNav, but was ${_topLevelNavigations.lastOrNull()}"
        }
    }

    fun assertNavigatedToFavorites() {
        check(_topLevelNavigations.lastOrNull() == FavoritesNav) {
            "Expected last top-level navigation to FavoritesNav, but was ${_topLevelNavigations.lastOrNull()}"
        }
    }

    /**
     * Assert that navigateBack was called.
     */
    fun assertNavigatedBack() {
        check(_navigatedBack.isNotEmpty()) {
            "Expected navigateBack to be called, but it was not"
        }
    }

    /**
     * Assert that navigateBack was NOT called.
     */
    fun assertDidNotNavigateBack() {
        check(_navigatedBack.isEmpty()) {
            "Expected navigateBack to NOT be called, but it was"
        }
    }

    /**
     * Get the last navigated movie ID (if any).
     */
    fun getLastNavigatedMovieId(): Int? = _navigatedToMovieIds.lastOrNull()

    /**
     * Get the count of navigation calls to movie details.
     */
    fun getNavigationCount(): Int = _navigatedToMovieIds.size
}