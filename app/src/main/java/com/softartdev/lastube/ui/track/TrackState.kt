package com.softartdev.lastube.ui.track

import com.softartdev.lastube.model.Resource
import com.softartdev.lastube.model.ResourceState

sealed class TrackState(
        val state: ResourceState,
        val trackResult: TrackResult? = null,
        val errorMessage: String? = null
): Resource<TrackResult>(state, trackResult, errorMessage) {

    data class Success(private val result: TrackResult) : TrackState(ResourceState.SUCCESS, result)

    data class Error(private val error: String? = null) : TrackState(ResourceState.ERROR, errorMessage = error)

    object Loading : TrackState(ResourceState.LOADING)
}