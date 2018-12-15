package com.softartdev.lastube.ui.chart

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import com.softartdev.lastube.model.ResultType

class ChartViewModelFactory(private val resultType: ResultType) : ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return modelClass.cast(ChartViewModel(resultType))
    }
}