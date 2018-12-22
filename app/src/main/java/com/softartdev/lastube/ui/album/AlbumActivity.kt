package com.softartdev.lastube.ui.album

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.browser.customtabs.CustomTabsIntent
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.softartdev.lastube.R
import com.softartdev.lastube.model.ResourceState
import com.softartdev.lastube.ui.widget.error.ErrorListener
import de.umass.lastfm.Track
import kotlinx.android.synthetic.main.activity_album.*
import kotlinx.android.synthetic.main.view_error.view.*

class AlbumActivity : AppCompatActivity(), Observer<AlbumState>, TrackClickListener, ErrorListener {

    private lateinit var viewModel: AlbumViewModel

    private val trackAdapter by lazy { TrackAdapter() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_album)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val artist = intent.getStringExtra(EXT_ARTIST)
        val album = intent.getStringExtra(EXT_ALBUM)
        supportActionBar?.title = "$artist - $album"

        album_recycler_view.apply {
            layoutManager = LinearLayoutManager(this@AlbumActivity)
            adapter = trackAdapter
        }
        trackAdapter.trackClickListener = this
        album_error_view.errorListener = this

        viewModel = ViewModelProviders.of(this, AlbumViewModelFactory(artist, album))[AlbumViewModel::class.java]
        viewModel.albumViewModel.observe(this, this)
    }

    override fun onChanged(albumState: AlbumState) {
        when(albumState.state) {
            ResourceState.LOADING -> {
                album_recycler_view.visibility = View.GONE
                album_progress_view.visibility = View.VISIBLE
                album_error_view.visibility = View.GONE
            }
            ResourceState.SUCCESS -> {
                album_recycler_view.visibility = View.VISIBLE
                album_progress_view.visibility = View.GONE
                album_error_view.visibility = View.GONE
                albumState.album?.tracks?.let { trackAdapter.track = it.toList() }
            }
            ResourceState.ERROR -> {
                album_recycler_view.visibility = View.GONE
                album_progress_view.visibility = View.GONE
                album_error_view.visibility = View.VISIBLE
                album_error_view.text_message.text = if (albumState.errorMessage.isNullOrEmpty()) {
                    getString(R.string.label_error_result)
                } else {
                    albumState.errorMessage
                }
            }
        }
    }
    
    override fun onTrackItemClick(track: Track) {
        CustomTabsIntent.Builder()
                .setToolbarColor(ContextCompat.getColor(this, R.color.primary))
                .build()
                .launchUrl(this, Uri.parse(track.url))
    }

    override fun onTryAgainClicked() = viewModel.getAlbum()
    
    override fun onOptionsItemSelected(item: MenuItem?): Boolean = when(item?.itemId) {
        android.R.id.home -> {
            finish()
            true
        }
        else -> super.onOptionsItemSelected(item)
    }

    companion object {
        private const val EXT_ARTIST = "ext_artist"
        private const val EXT_ALBUM = "ext_album"

        fun getIntent(context: Context, artist: String, album: String) =
                Intent(context, AlbumActivity::class.java).apply {
                    putExtra(EXT_ARTIST, artist)
                    putExtra(EXT_ALBUM, album)
                }
    }
}
