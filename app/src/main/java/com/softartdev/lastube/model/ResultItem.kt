package com.softartdev.lastube.model

data class ResultItem(
        val title: String,
        val subtitle: String,
        val imageUrl: String,
        val type: ResultType,
        val mbId: String
)