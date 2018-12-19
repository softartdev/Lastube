package com.softartdev.lastube.ui.track

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.softartdev.lastube.R
import com.softartdev.lastube.model.ResourceState
import com.softartdev.lastube.ui.widget.error.ErrorListener
import kotlinx.android.synthetic.main.activity_track.*
import kotlinx.android.synthetic.main.content_track.*
import kotlinx.android.synthetic.main.view_error.view.*

class TrackActivity : AppCompatActivity(), Observer<TrackState>, ErrorListener {

    private lateinit var viewModel: TrackViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_track)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val title = intent.getStringExtra(EXT_TITLE)
        val subtitle = intent.getStringExtra(EXT_SUBTITLE)
        supportActionBar?.title = "$title - $subtitle"

        track_error_view.errorListener = this

        viewModel = ViewModelProviders.of(this, TrackViewModelFactory(title, subtitle))[TrackViewModel::class.java]
        viewModel.trackLiveData.observe(this, this)
    }

    override fun onChanged(trackState: TrackState) {
        when(trackState.state) {
            ResourceState.LOADING -> {
                track_text_view.visibility = View.GONE
                track_progress_view.visibility = View.VISIBLE
                track_error_view.visibility = View.GONE
            }
            ResourceState.SUCCESS -> {
                track_text_view.visibility = View.VISIBLE
                track_progress_view.visibility = View.GONE
                track_error_view.visibility = View.GONE
                trackState.trackResult?.let { track_text_view.text = it.track?.wikiText }
            }
            ResourceState.ERROR -> {
                track_text_view.visibility = View.GONE
                track_progress_view.visibility = View.GONE
                track_error_view.visibility = View.VISIBLE
                track_error_view.text_message.text = if (trackState.errorMessage.isNullOrEmpty()) {
                    getString(R.string.label_error_result)
                } else {
                    trackState.errorMessage
                }
            }
        }
    }

    override fun onTryAgainClicked() = viewModel.getTrack()

    override fun onOptionsItemSelected(item: MenuItem?): Boolean = when(item?.itemId) {
        android.R.id.home -> {
            finish()
            true
        }
        else -> super.onOptionsItemSelected(item)
    }

    companion object {
        private const val EXT_TITLE = "ext_title"
        private const val EXT_SUBTITLE = "ext_subtitle"

        fun getIntent(context: Context, title: String, subtitle: String) =
                Intent(context, TrackActivity::class.java).apply {
                    putExtra(EXT_TITLE, title)
                    putExtra(EXT_SUBTITLE, subtitle)
                }
    }
}
