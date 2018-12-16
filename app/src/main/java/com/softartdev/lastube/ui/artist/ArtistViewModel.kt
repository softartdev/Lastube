package com.softartdev.lastube.ui.artist

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.softartdev.lastube.LastfmAPI
import de.umass.lastfm.Album
import de.umass.lastfm.Artist
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.functions.BiFunction
import io.reactivex.schedulers.Schedulers

class ArtistViewModel(
        private val artist: String
): ViewModel() {

    val artistLiveData: MutableLiveData<ArtistState> = MutableLiveData()

    private var disposable: Disposable? = null

    init {
        getArtist()
    }

    fun getArtist() {
        disposable?.dispose()
        disposable = Single.zip(
                Single.fromCallable { Artist.getInfo(artist, LastfmAPI.KEY) },
                Single.fromCallable { Artist.getTopAlbums(artist, LastfmAPI.KEY) },
                BiFunction<Artist, Collection<Album>, ArtistResult> {
                    artist, albums -> ArtistResult(artist, albums.toList())
                }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe { artistLiveData.postValue(ArtistState.Loading) }
                .subscribe({
                    artistLiveData.postValue(ArtistState.Success(it))
                }, {
                    it.printStackTrace()
                    artistLiveData.postValue(ArtistState.Error(it.message))
                })
    }

    override fun onCleared() {
        disposable?.dispose()
    }
}