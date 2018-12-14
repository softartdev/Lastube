package com.softartdev.lastube.ui.chart

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.softartdev.lastube.R
import com.squareup.picasso.Picasso
import de.umass.lastfm.Artist
import de.umass.lastfm.ImageSize
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.item_chart.*

class ChartAdapter : RecyclerView.Adapter<ChartAdapter.ViewHolder>() {

    var artists: List<Artist> = emptyList()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ViewHolder(
            containerView = LayoutInflater.from(parent.context).inflate(R.layout.item_chart, parent, false)
    )

    override fun getItemCount() = artists.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) = holder.bind(artists[position])

    inner class ViewHolder(override val containerView: View) : RecyclerView.ViewHolder(containerView), LayoutContainer {
        fun bind(artist: Artist) = with(containerView) {
            chart_title_text_view.text = artist.name
            chart_subtitle_text_view.apply {
                text = context.getString(R.string.scrobbles_count, artist.playcount)
            }
            Picasso.get().load(artist.getImageURL(ImageSize.MEGA)).into(chart_image_view)
        }
    }

}