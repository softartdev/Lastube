package com.softartdev.lastube.ui.track

import de.umass.lastfm.Album
import de.umass.lastfm.Artist
import de.umass.lastfm.Track

data class TrackResult(
        val track: Track?,
        val album: Album?,
        val artist: Artist?
)