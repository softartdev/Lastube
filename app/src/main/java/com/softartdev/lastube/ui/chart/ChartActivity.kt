package com.softartdev.lastube.ui.chart

import android.os.Bundle
import android.support.design.widget.TabLayout
import android.support.v7.app.AppCompatActivity
import com.softartdev.lastube.R
import kotlinx.android.synthetic.main.activity_chart.*

class ChartActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chart)

        setSupportActionBar(toolbar)
        container.adapter = ChartPagerAdapter(supportFragmentManager)

        container.addOnPageChangeListener(TabLayout.TabLayoutOnPageChangeListener(tabs))
        tabs.addOnTabSelectedListener(TabLayout.ViewPagerOnTabSelectedListener(container))
    }
}
