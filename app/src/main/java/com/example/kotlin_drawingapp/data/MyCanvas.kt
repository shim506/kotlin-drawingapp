package com.example.kotlin_drawingapp.data

import android.content.Context
import android.graphics.*
import android.util.Log
import android.util.TypedValue
import android.view.MotionEvent
import android.view.View
import com.example.kotlin_drawingapp.CanvasTouchListener
import com.orhanobut.logger.Logger

class MyCanvas(
    context: Context,
    private val listener: CanvasTouchListener
) : View(context) {

    var rect: RectF = RectF()
    private var selectedRectangles = mutableListOf<Rectangle>()
    private var rectangles = mutableListOf<Rectangle>()

    fun drawRectangle(recList: MutableList<Rectangle>) {
        rectangles = recList
        invalidate()
    }

    fun drawBound(recList: MutableList<Rectangle>) {
        selectedRectangles = recList
        invalidate()
    }

    public override fun onDraw(canvas: Canvas?) {
        selectedRectangles.forEach {
            val paint = setBorderPaint(it)
            rect = createRecF(it)
            canvas?.drawRect(rect, paint)
        }

        rectangles.forEach {
            val paint = setPaint(it)
            rect = createRecF(it)
            canvas?.drawRect(rect, paint)
        }
        super.onDraw(canvas)
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        event?.let {
            listener.onTouch(convertPxToDp(event.x.toInt()), convertPxToDp(event.y.toInt()))
        }
        return true
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

    private fun setBorderPaint(rec: Rectangle): Paint {
        val paint = Paint()
        paint.style = Paint.Style.STROKE
        paint.color = Color.BLACK
        paint.strokeWidth = 3.0F
        return paint
    }

    private fun setPaint(rec: Rectangle): Paint {
        val paint = Paint()
        paint.style = Paint.Style.FILL
        paint.color = Color.argb(rec.getAlpha(), rec.rgba.r, rec.rgba.g, rec.rgba.b)
        return paint
    }

    private fun createRecF(rectangle: Rectangle): RectF {
        val startX = convertDpToPx(rectangle.point.x).toFloat()
        val startY = convertDpToPx(rectangle.point.y).toFloat()
        val width = convertDpToPx(rectangle.size.width).toFloat()
        val height = convertDpToPx(rectangle.size.height).toFloat()

        return RectF(startX, startY, (startX + width), (startY + height))
    }


}
