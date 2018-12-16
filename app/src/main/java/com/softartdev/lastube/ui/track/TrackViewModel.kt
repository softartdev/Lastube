package com.softartdev.lastube.ui.track

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.softartdev.lastube.LastfmAPI
import de.umass.lastfm.Track
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

class TrackViewModel(
        private val artist: String,
        private val track: String
): ViewModel() {

    val trackLiveData: MutableLiveData<TrackState> = MutableLiveData()

    private var disposable: Disposable? = null

    init {
        getTrack()
    }

    fun getTrack() {
        disposable?.dispose()
        disposable = Single.fromCallable { Track.getInfo(artist, track, LastfmAPI.KEY) }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe { trackLiveData.postValue(TrackState.Loading) }
                .subscribe({
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