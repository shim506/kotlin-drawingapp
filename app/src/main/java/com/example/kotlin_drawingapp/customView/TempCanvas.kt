package com.example.kotlin_drawingapp.customView

import android.content.Context
import android.graphics.*
import com.example.kotlin_drawingapp.data.*
import com.example.kotlin_drawingapp.data.Picture
import com.example.kotlin_drawingapp.data.Point

const val ALPHA_VALUE = 0.5F

class TempCanvas(
    context: Context,
    val rectangle: Rectangle?,
    val dpClickX: Int,
    val dpClickY: Int
) : BaseCanvas(context) {
    private var rect: RectF = RectF()

    private var canvasObjectType: CanvasObjectType? = null
    var selectedPicture: Picture? = null
    var selectedText: Text? = null

    private var touchX = convertDpToPx(dpClickX)
    private var touchY = convertPxToDp(dpClickY)

    fun drawTempRectangle(pxX: Int, pxY: Int) {
        canvasObjectType = CanvasObjectType.RECTANGLE
        val (startX, startY) = getTempPosPx(pxX, pxY)
        rectangle?.let {
            val width = convertDpToPx(it.size.width)
            val height = convertDpToPx(it.size.height)
            rect = createRecF(startX, startY, width, height)
            invalidate()
        }
    }

    fun drawTempPicture(x: Int, y: Int, picture: Picture) {
        canvasObjectType = CanvasObjectType.PICTURE
        selectedPicture = picture
        touchX = x
        touchY = y
        invalidate()
    }

    fun drawTempText(x: Int, y: Int, selected: Text) {
        canvasObjectType = CanvasObjectType.TEXT
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

        when (canvasObjectType) {
            CanvasObjectType.PICTURE -> {
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
                    // Logger.d("픽쳐 ${it.getNumber()}")
                }
            }
            CanvasObjectType.RECTANGLE -> {
                rectangle?.let {
                    val paint = setLowerAlphaPaint(rectangle, ALPHA_VALUE)
                    canvas?.drawRect(rect, paint)
                    //Logger.d("렉텡 ${it.getNumber()}")
                }
            }
            CanvasObjectType.TEXT -> {
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
                    // Logger.d("텍스트 ${it.getNumber()}")
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

