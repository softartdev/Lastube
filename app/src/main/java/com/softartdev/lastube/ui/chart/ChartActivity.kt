package com.softartdev.lastube.ui.chart

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.softartdev.lastube.R

class ChartActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.chart_activity)
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                    .replace(R.id.container, ChartFragment())
                    .commitNow()
        }
    }

}
