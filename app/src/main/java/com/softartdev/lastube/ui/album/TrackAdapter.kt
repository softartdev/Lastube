package com.softartdev.lastube.ui.album

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.softartdev.lastube.R
import de.umass.lastfm.Track
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.item_track.*

class TrackAdapter : RecyclerView.Adapter<TrackAdapter.ViewHolder>() {

    var trackClickListener: TrackClickListener? = null

    var track: List<Track> = emptyList()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ViewHolder(
            containerView = LayoutInflater.from(parent.context).inflate(R.layout.item_track, parent, false)
    )

    override fun getItemCount() = track.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) = holder.bind(track[position])

    inner class ViewHolder(override val containerView: View) : RecyclerView.ViewHolder(containerView), LayoutContainer {
        fun bind(track: Track) = with(containerView) {
            track_text_view.text = track.name
            setOnClickListener { trackClickListener?.onTrackItemClick(track) }
        }
    }

}