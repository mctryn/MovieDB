package com.mctryn.moviedb.navigation

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.ContentTransform
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.animation.core.tween
import androidx.navigation3.scene.Scene
import androidx.navigation3.ui.NavDisplay

private const val NAV_ANIMATION_DURATION_MS = 300

fun movieDetailsNavigationTransitions(): Map<String, Any> =
    NavDisplay.transitionSpec {
        movieDetailsForwardTransition()
    } + NavDisplay.popTransitionSpec {
        movieDetailsBackTransition()
    } + NavDisplay.predictivePopTransitionSpec {
        movieDetailsBackTransition()
    }

private fun AnimatedContentTransitionScope<Scene<*>>.movieDetailsForwardTransition(): ContentTransform =
    slideIntoContainer(
        towards = AnimatedContentTransitionScope.SlideDirection.Left,
        animationSpec = tween(NAV_ANIMATION_DURATION_MS)
    ) + fadeIn(animationSpec = tween(NAV_ANIMATION_DURATION_MS)) togetherWith
            slideOutOfContainer(
                towards = AnimatedContentTransitionScope.SlideDirection.Left,
                animationSpec = tween(NAV_ANIMATION_DURATION_MS)
            ) + fadeOut(animationSpec = tween(NAV_ANIMATION_DURATION_MS))

private fun AnimatedContentTransitionScope<Scene<*>>.movieDetailsBackTransition(): ContentTransform =
    slideIntoContainer(
        towards = AnimatedContentTransitionScope.SlideDirection.Right,
        animationSpec = tween(NAV_ANIMATION_DURATION_MS)
    ) + fadeIn(animationSpec = tween(NAV_ANIMATION_DURATION_MS)) togetherWith
            slideOutOfContainer(
                towards = AnimatedContentTransitionScope.SlideDirection.Right,
                animationSpec = tween(NAV_ANIMATION_DURATION_MS)
            ) + fadeOut(animationSpec = tween(NAV_ANIMATION_DURATION_MS))
