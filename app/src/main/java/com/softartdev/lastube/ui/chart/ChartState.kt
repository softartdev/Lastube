package com.softartdev.lastube.ui.chart

import com.softartdev.lastube.model.ResourceState
import com.softartdev.lastube.model.ResultItem

sealed class ChartState(
        val resourceState: ResourceState,
        val data: List<ResultItem>? = null,
        val errorMessage: String? = null
) {

    data class Success(private val results: List<ResultItem>) : ChartState(ResourceState.SUCCESS, results)

    data class Error(private val message: String? = null) : ChartState(ResourceState.ERROR, errorMessage = message)

    object Loading : ChartState(ResourceState.LOADING)
}