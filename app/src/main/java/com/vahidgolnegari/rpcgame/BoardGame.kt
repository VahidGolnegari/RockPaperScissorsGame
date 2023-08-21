package com.vahidgolnegari.rpcgame

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.util.AttributeSet
import android.view.ViewGroup
import com.vahidgolnegari.rpcgame.gameItems.BaseGameItem
import com.vahidgolnegari.rpcgame.gameItems.PaperView
import com.vahidgolnegari.rpcgame.gameItems.RockView
import com.vahidgolnegari.rpcgame.gameItems.ScissorsView
import kotlinx.coroutines.*
import kotlin.math.roundToInt

class BoardGame(context: Context, attributeSet: AttributeSet) : ViewGroup(context, attributeSet) {

    private val borderPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.BLACK
        style = Paint.Style.STROKE
        strokeWidth = 2.dpToPx()
    }

    private var mCallback: IBoardGameListener? = null

    private val totalGameItems = 5
    var firstInit: Boolean = false
    private var gameJob: Job? = null

    init {
        setWillNotDraw(false)
        for (i in 0 until totalGameItems) {
            addView(RockView(context))
            addView(ScissorsView(context))
            addView(PaperView(context))
        }
    }

    fun setListener(listener: IBoardGameListener) {
        mCallback = listener
    }

    fun playPauseGame() {
        if (gameJob?.isActive == true) {
            gameJob?.cancel()
            gameJob = null
            mCallback?.onPlayStatusChanged(isPlaying = false)
        } else {
            startGameInternally()
        }
    }

    private fun startGameInternally() {
        gameJob = CoroutineScope(Dispatchers.Main).launch(start = CoroutineStart.LAZY) {
            while (true) {
                getNextDirection()
                delay(50L)
            }
        }
        gameJob?.start()
        mCallback?.onPlayStatusChanged(isPlaying = true)
    }

    fun forcePause() {
        if (gameJob?.isActive == true) {
            playPauseGame()
        }
    }

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        if (firstInit.not()) {
            for (index in 0 until childCount) {
                val child = getChildAt(index)
                if (child is BaseGameItem) {
                    child.currentX = (measuredWidth - child.fixedSize).randomNumberForThis()
                    child.currentY = (measuredHeight - child.fixedSize).randomNumberForThis()
                    child.angle = (0..360).random().toDouble()
                }
            }
            firstInit = true
        }

        for (index in 0 until childCount) {
            val child = getChildAt(index)
            if (child is BaseGameItem) {
                val left = child.currentX
                val top = child.currentY
                val right = left + child.fixedSize
                val bottom = top + child.fixedSize
                child.layout(left, top, right, bottom)
            }
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        val childCount = childCount
        for (i in 0 until childCount) {
            val child = getChildAt(i)
            child.measure(widthMeasureSpec, heightMeasureSpec)
        }
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        canvas.drawRect(0f, 0f, width.toFloat(), height.toFloat(), borderPaint)
    }

    private fun Int.dpToPx(): Float {
        return this * context.resources.displayMetrics.density
    }


    private fun getNextDirection() {
        for (childIndex in 0 until childCount) {
            val child = getChildAt(childIndex)
            if (child is BaseGameItem) {
                val offsetX = child.currentX
                val offsetY = child.currentY
                var teta = child.angle

                var x2 = (offsetX + child.r * teta.calculateCos()).roundToInt()
                var y2 = (offsetY + child.r * teta.calculateSin()).roundToInt()

                val minAngle = 5

                if (x2 + child.fixedSize >= measuredWidth) {
                    if (teta in (0.0..90.0)) {
                        teta = (90 + minAngle..180 - minAngle).random().toDouble()
                    } else if (teta in (270.0..360.0)) {
                        teta = (180 + minAngle..270 - minAngle).random().toDouble()
                    }
                    x2 = (offsetX + child.r * teta.calculateCos()).roundToInt()
                    y2 = (offsetY + child.r * teta.calculateSin()).roundToInt()
                }

                if (y2 + child.fixedSize >= measuredHeight) {
                    if (teta in (90.0..180.0)) {
                        teta = (180 + minAngle..270 - minAngle).random().toDouble()
                    } else if (teta in (0.0..90.0)) {
                        teta = (270 + minAngle..360 - minAngle).random().toDouble()
                    }
                    x2 = (offsetX + child.r * teta.calculateCos()).roundToInt()
                    y2 = (offsetY + child.r * teta.calculateSin()).roundToInt()
                }

                if (x2 <= 0) {
                    if (teta in (180.0..270.0)) {
                        teta = (270 + minAngle..360 - minAngle).random().toDouble()
                    } else if (teta in (90.0..180.0)) {
                        teta = (0 + minAngle..90 - minAngle).random().toDouble()
                    }
                    x2 = (offsetX + child.r * teta.calculateCos()).roundToInt()
                    y2 = (offsetY + child.r * teta.calculateSin()).roundToInt()
                }

                if (y2 <= 0) {
                    if (teta in (270.0..360.0)) {
                        teta = (0 + minAngle..90 - minAngle).random().toDouble()
                    } else if (teta in (180.0..270.0)) {
                        teta = (90 + minAngle..180 - minAngle).random().toDouble()
                    }
                    x2 = (offsetX + child.r * teta.calculateCos()).roundToInt()
                    y2 = (offsetY + child.r * teta.calculateSin()).roundToInt()
                }
                child.currentX = x2
                child.currentY = y2
                child.angle = teta
                checkViewCollision(child)
                requestLayout()
            }
        }
    }

    private fun changeTwoSameObjectDirection(vararg sameObjects: BaseGameItem) {
        sameObjects.forEach { sameObject ->
            val x0 = sameObject.currentX
            val y0 = sameObject.currentY
            var angle0 = sameObject.angle
            when (angle0) {
                in (0.0..180.0) -> {
                    angle0 += 180
                }

                in (180.0..360.0) -> {
                    angle0 -= 180
                }
            }

            val x = (x0 + sameObject.r * angle0.calculateCos()).roundToInt()
            val y = (y0 + sameObject.r * angle0.calculateSin()).roundToInt()

            sameObject.currentX = x
            sameObject.currentY = y
            sameObject.angle = angle0
        }
    }

    private fun checkViewCollision(
        view: BaseGameItem,
    ) {
        for (childIndex in 0 until childCount) {
            val tmpView = getChildAt(childIndex)
            if (tmpView is BaseGameItem) {
                if (view != tmpView) {
                    val isTopLefHit =
                        view.currentX in (tmpView.currentX..tmpView.currentX + view.fixedSize) &&
                                view.currentY in (tmpView.currentY..tmpView.currentY + view.fixedSize)

                    val isTopRightHit =
                        view.currentX + view.fixedSize in (tmpView.currentX..tmpView.currentX + view.fixedSize) &&
                                view.currentY in (tmpView.currentY..tmpView.currentY + view.fixedSize)

                    val isBottomLeftHit =
                        view.currentX in (tmpView.currentX..tmpView.currentX + view.fixedSize) &&
                                view.currentY + view.fixedSize in (tmpView.currentY..tmpView.currentY + view.fixedSize)

                    val isBottomRightHit =
                        view.currentX + view.fixedSize in (tmpView.currentX..tmpView.currentX + view.fixedSize) &&
                                view.currentY + view.fixedSize in (tmpView.currentY..tmpView.currentY + view.fixedSize)
                    if (
                        isTopLefHit || isTopRightHit || isBottomLeftHit || isBottomRightHit
                    ) {
                        if (view::class == tmpView::class) {
                            changeTwoSameObjectDirection(view, tmpView)
                        } else {
                            when (view) {
                                is RockView -> {
                                    if (tmpView is PaperView) {
                                        removeView(view)
                                    } else {
                                        removeView(tmpView)
                                    }
                                }
                                is PaperView -> {
                                    if (tmpView is RockView) {
                                        removeView(tmpView)
                                    } else {
                                        removeView(view)
                                    }
                                }
                                is ScissorsView -> {
                                    if (tmpView is RockView) {
                                        removeView(view)
                                    } else {
                                        removeView(tmpView)
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

interface IBoardGameListener {
    fun onPlayStatusChanged(isPlaying: Boolean)
}

