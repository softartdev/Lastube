package com.softartdev.lastube.ui.artist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class ArtistViewModelFactory(
        private val artist: String
) : ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return modelClass.cast(ArtistViewModel(artist)) ?: super.create(modelClass)
    }
}