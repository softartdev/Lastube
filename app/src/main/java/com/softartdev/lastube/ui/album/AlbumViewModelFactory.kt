package com.softartdev.lastube.ui.album

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class AlbumViewModelFactory(
        private val artist: String,
        private val album: String
) : ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return modelClass.cast(AlbumViewModel(artist, album)) ?: super.create(modelClass)
    }
}