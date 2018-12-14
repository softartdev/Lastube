package com.softartdev.lastube.ui.chart

import com.softartdev.lastube.model.ResourceState
import de.umass.lastfm.Artist

sealed class ChartState(
        val resourceState: ResourceState,
        val data: List<Artist>? = null,
        val errorMessage: String? = null
) {

    data class Success(private val bufferoos: List<Artist>): ChartState(ResourceState.SUCCESS,
            bufferoos)

    data class Error(private val message: String? = null): ChartState(ResourceState.ERROR,
            errorMessage = message)

    object Loading: ChartState(ResourceState.LOADING)
}