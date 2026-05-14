package com.mctryn.moviedb.navigation

import androidx.navigation3.runtime.NavEntry
import androidx.navigation3.runtime.NavKey

typealias AppEntryProvider = (NavKey) -> NavEntry<NavKey>
