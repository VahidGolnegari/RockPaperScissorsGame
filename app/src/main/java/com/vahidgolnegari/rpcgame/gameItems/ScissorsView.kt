package com.vahidgolnegari.rpcgame.gameItems

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import androidx.core.content.ContextCompat

class ScissorsView(context: Context?) : BaseGameItem(context) {

    private val crossPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = ContextCompat.getColor(context!!, android.R.color.holo_red_light)
        style = Paint.Style.STROKE
        strokeWidth = 4.dpToPx().toFloat()
    }

    override var currentX: Int = 0

    override var currentY: Int = 0

    override var angle: Double = 0.0

    override val r: Int = fixedSize / 2

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        val centerX = width / 2.toFloat()
        val centerY = height / 2.toFloat()
        val halfSize = fixedSize / 2.toFloat()

        canvas.drawLine(
            centerX - halfSize,
            centerY - halfSize,
            centerX + halfSize,
            centerY + halfSize,
            crossPaint
        )
        canvas.drawLine(
            centerX + halfSize,
            centerY - halfSize,
            centerX - halfSize,
            centerY + halfSize,
            crossPaint
        )
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        val desiredWidth = fixedSize
        val desiredHeight = fixedSize
        val width = resolveSizeAndState(desiredWidth, widthMeasureSpec, 0)
        val height = resolveSizeAndState(desiredHeight, heightMeasureSpec, 0)
        setMeasuredDimension(width, height)
    }
}