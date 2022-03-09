package com.example.kotlin_drawingapp

import android.content.Context
import android.graphics.*
import android.util.TypedValue
import android.view.View
import com.example.kotlin_drawingapp.data.Picture
import com.example.kotlin_drawingapp.data.Point
import com.example.kotlin_drawingapp.data.Rectangle
import com.orhanobut.logger.Logger

const val ALPHA_VALUE = 0.5F

class TempCanvas(
    context: Context,
    val rectangle: Rectangle?,
    val dpClickX: Int,
    val dpClickY: Int
) : BaseCanvas(context) {
    private var rect: RectF = RectF()

    var isPicture = false
    var selectedPicture: Picture? = null
    private var touchX = convertDpToPx(dpClickX)
    private var touchY = convertPxToDp(dpClickY)


    fun drawTempRec(pxX: Int, pxY: Int) {
        isPicture = false
        val (startX, startY) = getTempPosPx(pxX, pxY)
        rectangle?.let {
            val width = convertDpToPx(it.size.width)
            val height = convertDpToPx(it.size.height)
            rect = createRecF(startX, startY, width, height)
            invalidate()
        }
    }

    fun drawTempPic(x: Int, y: Int, picture: Picture) {
        isPicture = true
        touchX = x
        touchY = y
        selectedPicture = picture
        invalidate()
    }

    override fun onDraw(canvas: Canvas?) {
        if (isPicture) {
            selectedPicture?.let {
                with(it.rec) {
                    val bitmap = android.graphics.BitmapFactory.decodeByteArray(
                        it.byteArray,
                        0,
                        it.byteArray.size
                    )
                    val resizedBitmap = resizeBitmap(bitmap, this.size)
                    val paint = Paint()
                    paint.alpha = this.getAlpha()
                    val (startX, startY) = getTempPosPx(touchX, touchY)
                    canvas?.drawBitmap(
                        resizedBitmap,
                        startX.toFloat(),
                        startY.toFloat(),
                        paint
                    )
                }
            }
        } else {
            rectangle?.let {
                val paint = setLowerAlphaPaint(rectangle, ALPHA_VALUE)
                canvas?.drawRect(rect, paint)
            }
        }
        super.onDraw(canvas)
    }

    fun getTempPosPx(touchX: Int, touchY: Int): Point {
        // 기존 사각형의 클릭 위치에서 시작점까지의 거리
        val distX = (touchX - convertDpToPx(dpClickX))
        val distY = (touchY - convertDpToPx(dpClickY))
        return Point(
            convertDpToPx(rectangle?.point?.x ?: 0) + distX,
            convertDpToPx(rectangle?.point?.y ?: 0) + distY
        )
    }
}