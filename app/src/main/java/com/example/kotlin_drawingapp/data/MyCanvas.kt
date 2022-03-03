package com.example.kotlin_drawingapp.data

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.util.TypedValue
import android.view.MotionEvent
import android.view.View
import com.example.kotlin_drawingapp.CanvasTouchListener
import com.orhanobut.logger.Logger

class MyCanvas(
    context: Context,
    var selectedRectangle: Rectangle?,
    private val listener: CanvasTouchListener
) : View(context) {

    var rect: RectF = RectF()
    private var paint = Paint()

    fun drawRectangle(rec: Rectangle) = with(rec) {
        val startX = convertDpToPx(point.x)
        val startY = convertDpToPx(point.y)
        val width = convertDpToPx(size.width)
        val height = convertDpToPx(size.height)

        if (rec == selectedRectangle) {
            Logger.d("선택$selectedRectangle")
            paint.apply {
                this.style = Paint.Style.STROKE
                this.color = Color.BLACK

                this.style = Paint.Style.FILL
                this.color = Color.argb(getAlpha(), rgba.r, rgba.g, rgba.b)
            }
        } else {
            paint.apply {
                this.style = Paint.Style.FILL
                this.color = Color.argb(getAlpha(), rgba.r, rgba.g, rgba.b)
            }
        }
        rect = RectF(
            startX.toFloat(),
            startY.toFloat(),
            (startX + width).toFloat(),
            (startY + height).toFloat()
        )
        invalidate()
    }

    public override fun onDraw(canvas: Canvas?) {
        canvas?.drawRect(rect, paint)
        super.onDraw(canvas)
    }

    private fun convertDpToPx(value: Int): Int {
        return TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            value.toFloat(),
            resources.displayMetrics
        ).toInt()
    }

    private fun convertPxToDp(value: Int): Int {
        var density = context.resources.displayMetrics.density
        if (density.toDouble() == 1.0)
            density *= 4.0.toFloat() else if (density.toDouble() == 1.5)
            density *= (8 / 3).toFloat() else if (density.toDouble() == 2.0)
            density *= 2.0.toFloat()
        return (value / density).toInt()
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        event?.let {
            listener.onTouch(convertPxToDp(event.x.toInt()), convertPxToDp(event.y.toInt()))
        }
        return true
    }

    fun showSelectedRectangle(rec: Rectangle?) = with(rec) {
        removeOldSelectedRectangle()
        Logger.d(selectedRectangle)
        selectedRectangle = rec
        if (rec != null) {
            val startX = convertDpToPx(this!!.point.x)
            val startY = convertDpToPx(point.y)
            val width = convertDpToPx(size.width)
            val height = convertDpToPx(size.height)

            rect = RectF(
                startX.toFloat(),
                startY.toFloat(),
                (startX + width).toFloat(),
                (startY + height).toFloat()
            )
            paint.apply {
                this.style = Paint.Style.STROKE
                this.color = Color.RED
                this.strokeWidth = 3.0F
            }

            invalidate()
        }
    }

    private fun removeOldSelectedRectangle() {
        selectedRectangle?.let {
            drawRectangle(it)
        }
    }

    fun changeColor() {
        selectedRectangle?.let {
            paint.style = Paint.Style.FILL
            paint.color = Color.BLACK
            invalidate()
        }
    }

}
