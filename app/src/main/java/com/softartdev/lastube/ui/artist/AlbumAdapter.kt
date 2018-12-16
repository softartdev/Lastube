package com.softartdev.lastube.ui.artist

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.softartdev.lastube.R
import de.umass.lastfm.Album
import de.umass.lastfm.ImageSize
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.item_album.*

class AlbumAdapter : RecyclerView.Adapter<AlbumAdapter.ViewHolder>() {

    var albumClickListener: AlbumClickListener? = null

    var albums: List<Album> = emptyList()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ViewHolder(
            containerView = LayoutInflater.from(parent.context).inflate(R.layout.item_album, parent, false)
    )

    override fun getItemCount() = albums.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) = holder.bind(albums[position])

    inner class ViewHolder(override val containerView: View) : RecyclerView.ViewHolder(containerView), LayoutContainer {
        fun bind(album: Album) = with(containerView) {
            album_title_text_view.text = album.name
            album_subtitle_text_view.text = album.releaseDate?.toString()
            Glide.with(containerView).load(album.getImageURL(ImageSize.EXTRALARGE)).into(album_image_view)
            setOnClickListener { albumClickListener?.onChartItemClick(album) }
        }
    }

}