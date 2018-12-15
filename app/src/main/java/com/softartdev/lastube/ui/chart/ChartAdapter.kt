package com.softartdev.lastube.ui.chart

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.softartdev.lastube.R
import com.softartdev.lastube.model.ResultItem
import com.softartdev.lastube.model.ResultType
import com.squareup.picasso.Picasso
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.item_chart.*

class ChartAdapter : RecyclerView.Adapter<ChartAdapter.ViewHolder>() {

    var results: List<ResultItem> = emptyList()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ViewHolder(
            containerView = LayoutInflater.from(parent.context).inflate(R.layout.item_chart, parent, false)
    )

    override fun getItemCount() = results.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) = holder.bind(results[position])

    inner class ViewHolder(override val containerView: View) : RecyclerView.ViewHolder(containerView), LayoutContainer {
        fun bind(result: ResultItem) = with(containerView) {
            chart_title_text_view.text = result.title
            chart_subtitle_text_view.apply {
                text = when(result.type) {
                    ResultType.Artist -> context.getString(R.string.scrobbles_count, result.subtitle)
                    else -> result.subtitle
                }
            }
            Picasso.get().load(result.imageUrl).into(chart_image_view)
        }
    }

}