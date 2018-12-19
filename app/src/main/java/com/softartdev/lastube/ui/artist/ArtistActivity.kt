package com.softartdev.lastube.ui.artist

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.softartdev.lastube.R
import com.softartdev.lastube.model.ResourceState
import com.softartdev.lastube.model.ResultItem
import com.softartdev.lastube.ui.album.AlbumActivity
import com.softartdev.lastube.ui.widget.error.ErrorListener
import de.umass.lastfm.Album
import kotlinx.android.synthetic.main.activity_artist.*
import kotlinx.android.synthetic.main.content_artist.*
import kotlinx.android.synthetic.main.view_error.view.*
import timber.log.Timber

class ArtistActivity : AppCompatActivity(), Observer<ArtistState>, AlbumClickListener, ErrorListener {

    private lateinit var viewModel: ArtistViewModel

    private val albumAdapter by lazy { AlbumAdapter() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_artist)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val title = intent.getStringExtra(EXT_TITLE)
        supportActionBar?.title = title

        artist_recycler_view.apply {
            layoutManager = LinearLayoutManager(this@ArtistActivity)
            adapter = albumAdapter
        }
        albumAdapter.albumClickListener = this
        artist_error_view.errorListener = this

        viewModel = ViewModelProviders.of(this, ArtistViewModelFactory(title))[ArtistViewModel::class.java]
        viewModel.artistLiveData.observe(this, this)
    }

    override fun onChanged(artistState: ArtistState) {
        when(artistState.state) {
            ResourceState.LOADING -> {
                artist_text_view.visibility = View.GONE
                artist_progress_view.visibility = View.VISIBLE
                artist_error_view.visibility = View.GONE
            }
            ResourceState.SUCCESS -> {
                artist_text_view.visibility = View.VISIBLE
                artist_progress_view.visibility = View.GONE
                artist_error_view.visibility = View.GONE
                artistState.artistResult?.albums?.let { albumAdapter.albums = it }
                artistState.artistResult?.let { artist_text_view.text = it.artist.wikiText }
            }
            ResourceState.ERROR -> {
                artist_text_view.visibility = View.GONE
                artist_progress_view.visibility = View.GONE
                artist_error_view.visibility = View.VISIBLE
                artist_error_view.text_message.text = if (artistState.errorMessage.isNullOrEmpty()) {
                    getString(R.string.label_error_result)
                } else {
                    artistState.errorMessage
                }
            }
        }
    }

    override fun onChartItemClick(album: Album) {
        Timber.d(album.toString())
        startActivity(AlbumActivity.getIntent(this, album.artist, album.name))
    }

    override fun onTryAgainClicked() = viewModel.getArtist()

    override fun onOptionsItemSelected(item: MenuItem?): Boolean = when(item?.itemId) {
        android.R.id.home -> {
            finish()
            true
        }
        else -> super.onOptionsItemSelected(item)
    }

    companion object {
        private const val EXT_TITLE = "ext_title"

        fun getIntent(context: Context, resultItem: ResultItem) =
                Intent(context, ArtistActivity::class.java).apply {
                    putExtra(EXT_TITLE, resultItem.title)
                }
    }
}
