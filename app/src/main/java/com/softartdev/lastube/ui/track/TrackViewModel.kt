package com.softartdev.lastube.ui.track

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.softartdev.lastube.LastfmAPI
import de.umass.lastfm.Album
import de.umass.lastfm.Artist
import de.umass.lastfm.Track
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.functions.Function3
import io.reactivex.schedulers.Schedulers
import timber.log.Timber

class TrackViewModel(
        private val artist: String,
        private val track: String,
        private val mbId: String
) : ViewModel() {

    val trackLiveData: MutableLiveData<TrackState> = MutableLiveData()

    private var disposable: Disposable? = null

    init {
        getTrack()
    }

    fun getTrack() {
        disposable?.dispose()
        val trackOrMbid: String = if (mbId.isBlank() || mbId.isEmpty()) track else mbId
        disposable = Single.zip(
                Single.fromCallable { Track.getInfo(artist, trackOrMbid, LastfmAPI.KEY) },
                Observable.fromCallable { Artist.getTopAlbums(artist, LastfmAPI.KEY) }
                        .flatMapIterable { it }
                        .filter { findTrack(it) }
                        .toList(),
                Single.fromCallable { Artist.getInfo(artist, LastfmAPI.KEY) },
                Function3<Track, Collection<Album>, Artist, TrackResult> { resultTracks, resultAlbums, resultArtist ->
                    TrackResult(
                            track = resultTracks,
                            album = resultAlbums.firstOrNull(),
                            artist = resultArtist
                    )
                }
        )
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe { trackLiveData.postValue(TrackState.Loading) }
                .subscribe({
                    Timber.d(it.toString())
                    trackLiveData.postValue(TrackState.Success(it))
                }, {
                    it.printStackTrace()
                    trackLiveData.postValue(TrackState.Error(it.message))
                })

    }

    private fun findTrack(album: Album): Boolean {
        album.tracks?.forEach {
            if (it.mbid == mbId || it.name == track) {
                return true
            }
        }
        return false
    }

    override fun onCleared() {
        disposable?.dispose()
    }
}