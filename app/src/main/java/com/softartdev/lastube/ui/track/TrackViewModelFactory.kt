package com.softartdev.lastube.ui.track

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class TrackViewModelFactory(
        private val artist: String,
        private val track: String
) : ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return modelClass.cast(TrackViewModel(artist, track)) ?: super.create(modelClass)
    }
}