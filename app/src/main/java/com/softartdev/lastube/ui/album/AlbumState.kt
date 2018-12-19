package com.softartdev.lastube.ui.album

import com.softartdev.lastube.model.Resource
import com.softartdev.lastube.model.ResourceState
import de.umass.lastfm.Album

sealed class AlbumState(
        val state: ResourceState,
        val album: Album? = null,
        val errorMessage: String? = null
): Resource<Album>(state, album, errorMessage) {

    data class Success(private val result: Album) : AlbumState(ResourceState.SUCCESS, result)

    data class Error(private val error: String? = null) : AlbumState(ResourceState.ERROR, errorMessage = error)

    object Loading : AlbumState(ResourceState.LOADING)
}