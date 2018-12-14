package com.softartdev.lastube.ui.chart

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.softartdev.lastube.R
import com.softartdev.lastube.model.ResourceState
import com.softartdev.lastube.ui.widget.empty.EmptyListener
import com.softartdev.lastube.ui.widget.error.ErrorListener
import kotlinx.android.synthetic.main.chart_fragment.*
import kotlinx.android.synthetic.main.view_error.view.*

class ChartFragment : Fragment(), EmptyListener, ErrorListener, SwipeRefreshLayout.OnRefreshListener {

    private val chartAdapter: ChartAdapter by lazy { ChartAdapter() }

    private lateinit var viewModel: ChartViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View =
            inflater.inflate(R.layout.chart_fragment, container, false)

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(ChartViewModel::class.java)
        viewModel.chartLiveData.observe(this, Observer { chartState ->
            chartState?.let { handleChartState(it) }
        })
        chart_swipe_refresh_layout.setOnRefreshListener(this)
        chart_recycler_view.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = chartAdapter
        }
        chart_empty_view.emptyListener = this
        chart_error_view.errorListener = this
        if (savedInstanceState == null) {
            viewModel.getArtistsChart()
        }
    }

    private fun handleChartState(chartState: ChartState) = when (chartState.resourceState) {
        ResourceState.LOADING -> {
            chart_progress_view.visibility = if (chart_swipe_refresh_layout.isRefreshing) View.GONE else View.VISIBLE
            chart_recycler_view.visibility = View.GONE
            chart_empty_view.visibility = View.GONE
            chart_error_view.visibility = View.GONE
        }
        ResourceState.SUCCESS -> {
            chart_swipe_refresh_layout.isRefreshing = false
            chart_recycler_view.visibility = if (chartState.data.isNullOrEmpty()) View.GONE else View.VISIBLE
            chart_progress_view.visibility = View.GONE
            chart_empty_view.visibility = if (chartState.data.isNullOrEmpty()) View.VISIBLE else View.GONE
            chart_error_view.visibility = View.GONE
            chartState.data?.let { chartAdapter.artists = it }
        }
        ResourceState.ERROR -> {
            chart_swipe_refresh_layout.isRefreshing = false
            chart_recycler_view.visibility = View.GONE
            chart_progress_view.visibility = View.GONE
            chart_empty_view.visibility = View.GONE
            chart_error_view.visibility = View.VISIBLE
            chart_error_view.text_message.text = if (chartState.errorMessage.isNullOrEmpty()) {
                getString(R.string.label_error_result)
            } else {
                chartState.errorMessage
            }
        }
    }

    override fun onRefresh() = viewModel.getArtistsChart()
    override fun onCheckAgainClicked() = viewModel.getArtistsChart()
    override fun onTryAgainClicked() = viewModel.getArtistsChart()
}
