package com.softartdev.lastube.ui.album

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.softartdev.lastube.LastfmAPI
import de.umass.lastfm.Album
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

class AlbumViewModel(
        private val artist: String,
        private val album: String
): ViewModel() {

    val albumViewModel: MutableLiveData<AlbumState> = MutableLiveData()

    private var disposable: Disposable? = null

    init {
        getAlbum()
    }

    fun getAlbum() {
        disposable?.dispose()
        disposable = Single.fromCallable { Album.getInfo(artist, album, LastfmAPI.KEY) }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe { albumViewModel.postValue(AlbumState.Loading) }
                .subscribe({
                    albumViewModel.postValue(AlbumState.Success(it))
                }, {
                    it.printStackTrace()
                    albumViewModel.postValue(AlbumState.Error(it.message))
                })
    }

    override fun onCleared() {
        disposable?.dispose()
    }
}