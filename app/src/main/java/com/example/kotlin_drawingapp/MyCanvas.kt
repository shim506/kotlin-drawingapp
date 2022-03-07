package com.example.kotlin_drawingapp

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.util.TypedValue
import android.view.MotionEvent
import android.view.View
import com.example.kotlin_drawingapp.data.Rectangle

class MyCanvas(
    context: Context,
    private val listener: CanvasTouchListener,
    private val measureListener: CanvasSizeListener
) : View(context) {

    private var rect: RectF = RectF()
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

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        val widthSize = convertPxToDp(MeasureSpec.getSize(widthMeasureSpec))
        val heightSize = convertPxToDp(MeasureSpec.getSize(heightMeasureSpec))
        measureListener.onMeasure(widthSize, heightSize)
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
