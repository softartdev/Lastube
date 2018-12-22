package com.softartdev.lastube.ui.artist

import de.umass.lastfm.Album

interface AlbumClickListener {
    fun onChartItemClick(album: Album)
}