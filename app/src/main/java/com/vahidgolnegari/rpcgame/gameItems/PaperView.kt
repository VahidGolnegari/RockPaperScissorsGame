package com.vahidgolnegari.rpcgame.gameItems

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import androidx.core.content.ContextCompat

class PaperView(context: Context?) : BaseGameItem(context) {
    private val rectanglePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = ContextCompat.getColor(context!!, android.R.color.holo_green_light)
        style = Paint.Style.FILL
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        val left = 0f
        val top = 0f
        val right = width.toFloat()
        val bottom = height.toFloat()

        canvas.drawRect(left, top, right, bottom, rectanglePaint)
    }

    override var currentY: Int = 0

    override var currentX: Int = 0

    override var angle: Double = 0.0

    override val r: Int = fixedSize / 2

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        val width = resolveSizeAndState(fixedSize, widthMeasureSpec, 0)
        val height = resolveSizeAndState(fixedSize, heightMeasureSpec, 0)
        setMeasuredDimension(width, height)
    }
}