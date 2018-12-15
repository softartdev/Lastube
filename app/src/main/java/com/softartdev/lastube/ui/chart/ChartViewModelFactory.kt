package com.softartdev.lastube.ui.chart

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.softartdev.lastube.model.ResultType

class ChartViewModelFactory(private val resultType: ResultType) : ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return modelClass.cast(ChartViewModel(resultType)) ?: super.create(modelClass)
    }
}