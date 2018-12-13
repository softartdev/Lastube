package com.softartdev.lastube.ui.chart

import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.softartdev.lastube.R

class ChartFragment : Fragment() {

    private lateinit var viewModel: ChartViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,savedInstanceState: Bundle?): View =
            inflater.inflate(R.layout.chart_fragment, container, false)

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(ChartViewModel::class.java)
        // TODO: Use the ViewModel
    }

}
