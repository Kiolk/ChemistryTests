package com.github.kiolk.chemistrytests.utils

import android.graphics.Color
import android.graphics.drawable.GradientDrawable

class SomeDrawable(orientation: Orientation? = Orientation.BOTTOM_TOP, colors: IntArray? = intArrayOf(Color.RED, Color.BLUE, Color.GREEN)) : GradientDrawable(orientation, colors) {

    init {
//        setStroke()
    }
}
