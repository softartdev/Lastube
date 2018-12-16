package com.softartdev.lastube.ui.artist

import de.umass.lastfm.Album
import de.umass.lastfm.Artist

data class ArtistResult(
        val artist: Artist,
        val albums: List<Album>
)