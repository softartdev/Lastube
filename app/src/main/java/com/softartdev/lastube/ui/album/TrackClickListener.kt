package com.softartdev.lastube.ui.album

import de.umass.lastfm.Track

interface TrackClickListener {
    fun onTrackItemClick(track: Track)
}