package com.example.kotlin_drawingapp.data

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.util.AttributeSet
import android.util.TypedValue
import android.view.MotionEvent
import android.view.View
import com.orhanobut.logger.Logger

class MyCanvas(context: Context) : View(context) {

    var rect: RectF = RectF()
    private val paint = Paint()

    fun drawRectangle(rec: Rectangle) = with(rec) {
        val startX = convertDpToPx(point.x)
        val startY = convertDpToPx(point.y)
        val width = convertDpToPx(size.width)
        val height = convertDpToPx(size.height)

        rect = RectF(
            startX.toFloat(),
            startY.toFloat(),
            (startX + width).toFloat(),
            (startY + height).toFloat()
        )
        paint.color = Color.argb(getAlpha(), rgba.r, rgba.g, rgba.b)
        invalidate()
    }

    public override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        canvas?.drawRect(rect, paint)
    }

    private fun convertDpToPx(value: Int): Int {
        return TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            value.toFloat(),
            resources.displayMetrics
        ).toInt()
    }

}