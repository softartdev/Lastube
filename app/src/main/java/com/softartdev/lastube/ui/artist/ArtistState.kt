package com.softartdev.lastube.ui.artist

import com.softartdev.lastube.model.Resource
import com.softartdev.lastube.model.ResourceState

sealed class ArtistState(
        val state: ResourceState,
        val artistResult: ArtistResult? = null,
        val errorMessage: String? = null
): Resource<ArtistResult>(state, artistResult, errorMessage) {

    data class Success(private val result: ArtistResult) : ArtistState(ResourceState.SUCCESS, result)

    data class Error(private val error: String? = null) : ArtistState(ResourceState.ERROR, errorMessage = error)

    object Loading : ArtistState(ResourceState.LOADING)
}