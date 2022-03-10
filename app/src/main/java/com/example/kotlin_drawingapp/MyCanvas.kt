package com.example.kotlin_drawingapp

import android.content.Context
import android.graphics.*
import android.view.MotionEvent
import com.example.kotlin_drawingapp.data.Picture
import com.example.kotlin_drawingapp.data.Rectangle


class MyCanvas(
    context: Context,
    private val touchListener: CanvasTouchListener,
    private val measureListener: CanvasSizeListener,

    ) : BaseCanvas(context) {


    private var rect: RectF = RectF()
    private var selectedRectangles = mutableListOf<Rectangle>()
    private var rectangles = mutableListOf<Rectangle>()
    private var pictureList = mutableListOf<Picture>()

    fun drawRectangle(recList: MutableList<Rectangle>) {
        rectangles = recList
        invalidate()
    }

    fun drawBound(recList: MutableList<Rectangle>) {
        selectedRectangles = recList
        invalidate()
    }

    fun drawImage(imageList: MutableList<Picture>) {
        pictureList = imageList
        invalidate()
    }

    fun drawAll(
        rectangleList: MutableList<Rectangle>,
        pictures: MutableList<Picture>,
        selectedRecList: MutableList<Rectangle>
    ) {
        rectangles = rectangleList
        selectedRectangles = selectedRecList
        this.pictureList = pictures
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

        pictureList.forEach {
            with(it.rec) {
                val bitmap = BitmapFactory.decodeByteArray(it.byteArray, 0, it.byteArray.size)
                val resizedBitmap = resizeBitmap(bitmap, this.size)
                val paint = Paint()
                paint.alpha = this.getAlpha()
                val (startX, startY) = Pair(
                    convertDpToPx(this.point.x).toFloat(),
                    convertDpToPx(this.point.y).toFloat()
                )
                canvas?.drawBitmap(
                    resizedBitmap,
                    startX,
                    startY,
                    paint
                )
            }
        }
        super.onDraw(canvas)
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        when (event?.action) {
            MotionEvent.ACTION_DOWN -> {
                event?.let {
                    touchListener.onTouchDown(
                        convertPxToDp(event.x.toInt()),
                        convertPxToDp(event.y.toInt())
                    )
                }
            }
            MotionEvent.ACTION_MOVE -> {
                touchListener.onMove(event.x.toInt(), event.y.toInt())
            }
            MotionEvent.ACTION_UP -> {
                touchListener.onTouchUP(event.x.toInt(), event.y.toInt())
            }
        }
        return true
    }

    private fun setBorderPaint(rec: Rectangle): Paint {
        val paint = Paint()
        paint.style = Paint.Style.STROKE
        paint.color = Color.BLACK
        paint.strokeWidth = 3.0F
        return paint
    }


}
