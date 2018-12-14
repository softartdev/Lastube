package com.softartdev.lastube.ui.chart

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import com.softartdev.lastube.LastfmAPI
import de.umass.lastfm.Chart
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

class ChartViewModel : ViewModel() {

    val chartLiveData: MutableLiveData<ChartState> = MutableLiveData()

    private var disposable: Disposable? = null

    fun getArtistsChart() {
        chartLiveData.postValue(ChartState.Loading)
        disposable = Single.fromCallable { Chart.getTopArtists(LastfmAPI.key) }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    chartLiveData.postValue(ChartState.Success(it.pageResults.toList()))
                }, {
                    it.printStackTrace()
                    chartLiveData.postValue(ChartState.Error(it.message))
                })
    }

    override fun onCleared() {
        disposable?.dispose()
    }
}
