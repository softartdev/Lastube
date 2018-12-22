package com.softartdev.lastube.ui.track

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class TrackViewModelFactory(
        private val artist: String,
        private val track: String,
        private val mbId: String
) : ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return modelClass.cast(TrackViewModel(artist, track, mbId)) ?: super.create(modelClass)
    }
}