package com.softartdev.lastube.ui.track

import com.softartdev.lastube.model.Resource
import com.softartdev.lastube.model.ResourceState
import de.umass.lastfm.Track

sealed class TrackState(
        val state: ResourceState,
        val track: Track? = null,
        val errorMessage: String? = null
): Resource<Track>(state, track, errorMessage) {

    data class Success(private val result: Track) : TrackState(ResourceState.SUCCESS, result)

    data class Error(private val error: String? = null) : TrackState(ResourceState.ERROR, errorMessage = error)

    object Loading : TrackState(ResourceState.LOADING)
}