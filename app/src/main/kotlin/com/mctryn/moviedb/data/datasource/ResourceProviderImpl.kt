package com.mctryn.moviedb.data.datasource

import android.content.Context
import java.io.InputStream

/**
 * Implementation of ResourceProvider that uses Android Context.
 * 
 * This is in the data layer (infrastructure) because it depends on Android Context.
 */
class ResourceProviderImpl(
    private val context: Context
) : ResourceProvider {
    
    override fun openRawResource(resourceId: Int): InputStream {
        return context.resources.openRawResource(resourceId)
    }
}