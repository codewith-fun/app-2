package com.mcode.app2.interfaces

import android.view.MotionEvent

interface DrawableClickListener {
    enum class DrawablePosition {
        TOP, BOTTOM, LEFT, RIGHT
    }

    fun onClick(target: DrawablePosition?,motion: MotionEvent?)
}