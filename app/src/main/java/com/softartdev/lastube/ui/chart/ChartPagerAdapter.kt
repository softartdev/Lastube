package com.softartdev.lastube.ui.chart

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter

class ChartPagerAdapter(fm: FragmentManager) : FragmentPagerAdapter(fm) {
    override fun getItem(position: Int): Fragment = ChartFragment().apply {
        arguments = Bundle().apply {
            putInt(ChartFragment.POS_RESULT_TYPE, position)
        }
    }
    override fun getCount(): Int = 2
}