package com.vahidgolnegari.rpcgame.gameItems

import android.content.Context
import android.view.View

abstract class BaseGameItem(context: Context) : View(context) {

    open var currentX: Int = 0
    open var currentY: Int = 0
    open var angle: Double = 0.0
    open var fixedSize: Int = 40.dpToPx()
    open val r = 10.dpToPx()

    fun Int.dpToPx(): Int {
        return this * (context.resources.displayMetrics.density).toInt()
    }
}