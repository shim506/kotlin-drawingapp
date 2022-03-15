package com.example.kotlin_drawingapp.customView

import android.content.Context
import android.graphics.*
import com.example.kotlin_drawingapp.data.*
import com.example.kotlin_drawingapp.data.Picture
import com.example.kotlin_drawingapp.data.Point
import kotlinx.coroutines.yield

const val ALPHA_VALUE = 0.5F

class TempCanvas(
    context: Context,
    val rectangle: Rectangle?,
    val dpClickX: Int,
    val dpClickY: Int
) : BaseCanvas(context) {
    private var rect: RectF = RectF()

    private var selectedType: SelectedType? = null
    var selectedPicture: Picture? = null
    var selectedText: Text? = null

    private var touchX = convertDpToPx(dpClickX)
    private var touchY = convertPxToDp(dpClickY)

    fun drawTempRectangle(pxX: Int, pxY: Int) {
        selectedType = SelectedType.RECTANGLE
        val (startX, startY) = getTempPosPx(pxX, pxY)
        rectangle?.let {
            val width = convertDpToPx(it.size.width)
            val height = convertDpToPx(it.size.height)
            rect = createRecF(startX, startY, width, height)
            invalidate()
        }
    }

    fun drawTempPicture(x: Int, y: Int, picture: Picture) {
        selectedType = SelectedType.PICTURE
        selectedPicture = picture
        touchX = x
        touchY = y
        invalidate()
    }

    fun drawTempText(x: Int, y: Int, selected: Text) {
        selectedType = SelectedType.TEXT
        selectedText = selected
        touchX = x
        touchY = y
        invalidate()

    }

    fun getTempAttrDP(pxX: Int, pxY: Int): Pair<Point, Size?> {
        val point = getTempPosPx(pxX, pxY)
        return Pair(point, rectangle?.size)
    }

    override fun onDraw(canvas: Canvas?) {

        when (selectedType) {
            SelectedType.PICTURE -> {
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
            }
            SelectedType.RECTANGLE -> {
                rectangle?.let {
                    val paint = setLowerAlphaPaint(rectangle, ALPHA_VALUE)
                    canvas?.drawRect(rect, paint)
                }
            }
            SelectedType.TEXT -> {
                val textPaint = Paint()
                textPaint.textSize = 50F
                val tempPoint = getTempPosPx(touchX, touchY)
                selectedText?.let {
                    canvas?.drawText(
                        it.text,
                        tempPoint.x.toFloat(),
                        ((tempPoint.y) + TEXT_SIZE).toFloat(),
                        textPaint
                    )
                }
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

enum class SelectedType {
    PICTURE, RECTANGLE, TEXT
}