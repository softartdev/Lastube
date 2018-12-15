package com.softartdev.lastube.ui.chart

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter

class ChartPagerAdapter(fm: FragmentManager) : FragmentPagerAdapter(fm) {
    override fun getItem(position: Int): Fragment = ChartFragment().apply {
        arguments = Bundle().apply {
            putInt(ChartFragment.POS_RESULT_TYPE, position)
        }
    }
    override fun getCount(): Int = 2
}