package com.vahidgolnegari.rpcgame.gameItems

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint

class RockView(context: Context?) : BaseGameItem(context) {
    private val circlePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.BLUE
        style = Paint.Style.FILL
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        val centerX = width / 2.toFloat()
        val centerY = height / 2.toFloat()
        val radius = fixedSize / 2.toFloat()

        canvas.drawCircle(centerX, centerY, radius, circlePaint)
    }

    override var currentX: Int = 0

    override var currentY: Int = 0

    override var angle: Double = 0.0

    override val r: Int = fixedSize / 2

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        val desiredSize = fixedSize
        val width = resolveSizeAndState(desiredSize, widthMeasureSpec, 0)
        val height = resolveSizeAndState(desiredSize, heightMeasureSpec, 0)
        setMeasuredDimension(width, height)
    }
}