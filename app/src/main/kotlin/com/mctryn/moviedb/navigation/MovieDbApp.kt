package com.mctryn.moviedb.navigation

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.runtime.rememberSaveableStateHolderNavEntryDecorator
import androidx.navigation3.ui.NavDisplay
import com.mctryn.moviedb.presentation.theme.MovieDBTheme
import org.koin.compose.koinInject
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember

@Composable
fun MovieDbApp(
    modifier: Modifier = Modifier
) {
    MovieDBTheme {
        val navigationManager = koinInject<NavigationManager>()
        val currentTopLevel = remember { mutableStateOf<NavKey>(MovieListNav) }

        val backStack = rememberNavBackStack(currentTopLevel.value)

        LaunchedEffect(backStack) {
            if (navigationManager is BackStackNavigationManager) {
                navigationManager.attachBackStack(backStack)
            }
        }

        val provider = koinInject<AppEntryProvider>()

        Scaffold(
            modifier = modifier,
        ) { paddingValues ->
            NavDisplay(
                backStack = backStack,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                entryDecorators = listOf(
                    rememberSaveableStateHolderNavEntryDecorator()
                ),
                onBack = {
                    if (navigationManager.canNavigateBack()) {
                        navigationManager.navigateBack()
                    }
                },
                entryProvider = provider
            )
        }
    }
}
