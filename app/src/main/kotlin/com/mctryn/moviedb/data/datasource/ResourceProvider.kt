package com.mctryn.moviedb.data.datasource

import java.io.InputStream

/**
 * Interface for providing access to raw resources.
 * 
 * This interface is in the data layer (infrastructure) because it
 * depends on Android-specific APIs (Context, resources).
 */
interface ResourceProvider {
    
    /**
     * Open a raw resource by ID.
     * 
     * @param resourceId The raw resource identifier
     * @return InputStream for reading the resource
     */
    fun openRawResource(resourceId: Int): InputStream
}