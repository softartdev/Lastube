package com.softartdev.lastube.ui.chart

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.softartdev.lastube.R
import com.softartdev.lastube.model.ResourceState
import com.softartdev.lastube.model.ResultItem
import com.softartdev.lastube.model.ResultType
import com.softartdev.lastube.ui.artist.ArtistActivity
import com.softartdev.lastube.ui.track.TrackActivity
import com.softartdev.lastube.ui.widget.empty.EmptyListener
import com.softartdev.lastube.ui.widget.error.ErrorListener
import kotlinx.android.synthetic.main.chart_fragment.*
import kotlinx.android.synthetic.main.view_error.view.*

class ChartFragment : Fragment(), Observer<ChartState>, EmptyListener, ErrorListener, SwipeRefreshLayout.OnRefreshListener, ChartClickListener {

    private val chartAdapter: ChartAdapter by lazy { ChartAdapter() }

    private lateinit var viewModel: ChartViewModel

    private lateinit var resultType: ResultType

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        resultType = ResultType.values()[arguments?.getInt(POS_RESULT_TYPE)
                ?: throw IllegalArgumentException("ChartFragment requires a result type@")]
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View =
            inflater.inflate(R.layout.chart_fragment, container, false)

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this, ChartViewModelFactory(resultType)).get(ChartViewModel::class.java)
        viewModel.chartLiveData.observe(this, this)
        chart_swipe_refresh_layout.setOnRefreshListener(this)
        chart_recycler_view.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = chartAdapter
        }
        chartAdapter.chartClickListener = this
        chart_empty_view.emptyListener = this
        chart_error_view.errorListener = this
    }

    override fun onChanged(chartState: ChartState) {
        when (chartState.resourceState) {
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
                chartState.data?.let { chartAdapter.results = it }
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
    }

    override fun onChartItemClick(resultItem: ResultItem) = startActivity(when(resultItem.type) {
        ResultType.Track -> TrackActivity.getIntent(requireContext(), resultItem.title, resultItem.subtitle, resultItem.mbId)
        ResultType.Artist -> ArtistActivity.getIntent(requireContext(), resultItem)
    })

    override fun onRefresh() = viewModel.getChart()
    override fun onCheckAgainClicked() = viewModel.getChart()
    override fun onTryAgainClicked() = viewModel.getChart()

    companion object {
        const val POS_RESULT_TYPE = "result_type"
    }
}
