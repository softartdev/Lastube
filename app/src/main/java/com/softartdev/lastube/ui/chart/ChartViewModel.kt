package com.softartdev.lastube.ui.chart

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.softartdev.lastube.LastfmAPI
import com.softartdev.lastube.model.ResultItem
import com.softartdev.lastube.model.ResultType
import de.umass.lastfm.Chart
import de.umass.lastfm.ImageSize
import de.umass.lastfm.Track
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

class ChartViewModel(private val resultType: ResultType) : ViewModel() {

    val chartLiveData: MutableLiveData<ChartState> = MutableLiveData()

    private var disposable: Disposable? = null

    init {
        getChart()
    }

    fun getChart() {
        disposable?.dispose()
        disposable = Single.fromCallable {
            when (resultType) {
                ResultType.Artist -> Chart.getTopArtists(LastfmAPI.KEY)
                ResultType.Track -> Chart.getTopTracks(LastfmAPI.KEY)
            }
        }.toObservable().flatMapIterable { it.pageResults }
                .observeOn(Schedulers.computation())
                .map {
                    ResultItem(
                            title = it.name,
                            subtitle = when (resultType) {
                                ResultType.Artist -> it.playcount.toString()
                                ResultType.Track -> (it as Track).artist
                            },
                            imageUrl = it.getImageURL(ImageSize.EXTRALARGE)
                                    ?: it.getImageURL(ImageSize.MEGA)
                                    ?: it.getImageURL(it.availableSizes().last()),
                            type = resultType,
                            mbId = it.mbid
                    )
                }.toList()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe { chartLiveData.postValue(ChartState.Loading) }
                .subscribe({
                    chartLiveData.postValue(ChartState.Success(it))
                }, {
                    it.printStackTrace()
                    chartLiveData.postValue(ChartState.Error(it.message))
                })
    }

    override fun onCleared() {
        disposable?.dispose()
    }
}
