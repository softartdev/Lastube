package com.softartdev.lastube.ui.chart

import com.softartdev.lastube.model.ResultItem

interface ChartClickListener {
    fun onChartItemClick(resultItem: ResultItem)
}