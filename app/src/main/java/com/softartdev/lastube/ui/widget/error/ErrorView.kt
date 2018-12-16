package com.softartdev.lastube.ui.widget.error

import android.content.Context
import android.util.AttributeSet
import android.view.Gravity
import android.view.LayoutInflater
import android.widget.LinearLayout
import com.softartdev.lastube.R
import kotlinx.android.synthetic.main.view_error.view.*

/**
 * Widget used to display an empty state to the user
 */
class ErrorView : LinearLayout {

    var errorListener: ErrorListener? = null

    constructor(context: Context) : super(context) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        init()
    }

    private fun init() {
        orientation = LinearLayout.VERTICAL
        gravity = Gravity.CENTER
        LayoutInflater.from(context).inflate(R.layout.view_error, this)
        button_try_again.setOnClickListener { errorListener?.onTryAgainClicked() }
    }

}