package com.mctryn.moviedb.navigation

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
    
    private val _navigatedBack = mutableListOf<Unit>()
    val navigatedBack: List<Unit> = _navigatedBack
    
    private var _canNavigateBack = false
    val canNavigateBack: Boolean get() = _canNavigateBack

    override fun navigateToMovieDetails(movieId: Int) {
        _navigatedToMovieIds.add(movieId)
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
     * Assert that navigation to movie details was NOT called.
     */
    fun assertNoNavigation() {
        check(_navigatedToMovieIds.isEmpty()) {
            "Expected no navigation, but got ${_navigatedToMovieIds.size} navigation(s): $_navigatedToMovieIds"
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