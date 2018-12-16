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
        private val track: String
) : ViewModel() {

    val trackLiveData: MutableLiveData<TrackState> = MutableLiveData()

    private var disposable: Disposable? = null

    init {
        getTrack()
    }

    fun getTrack() {
        disposable?.dispose()
        disposable = Single.zip(
                Single.fromCallable { Track.getInfo(artist, track, LastfmAPI.KEY) },
                Observable.fromCallable { Artist.getTopAlbums(artist, LastfmAPI.KEY) }
                        .flatMapIterable { it }
                        .filter { it.name == track }
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

    override fun onCleared() {
        disposable?.dispose()
    }
}