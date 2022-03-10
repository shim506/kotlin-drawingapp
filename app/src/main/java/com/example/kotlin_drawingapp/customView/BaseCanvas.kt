package com.example.kotlin_drawingapp.customView

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.util.TypedValue
import android.view.View
import com.example.kotlin_drawingapp.data.Rectangle
import com.example.kotlin_drawingapp.data.Size

open class BaseCanvas(context: Context) : View(context) {

    fun createRecF(pxX: Int, pxY: Int, pxWidth: Int, pxHeight: Int): RectF {
        return RectF(
            pxX.toFloat(),
            pxY.toFloat(),
            pxX + pxWidth.toFloat(),
            pxY + pxHeight.toFloat()
        )
    }

    fun createRecF(rectangle: Rectangle): RectF {
        val startX = convertDpToPx(rectangle.point.x).toFloat()
        val startY = convertDpToPx(rectangle.point.y).toFloat()
        val width = convertDpToPx(rectangle.size.width).toFloat()
        val height = convertDpToPx(rectangle.size.height).toFloat()
        return RectF(startX, startY, (startX + width), (startY + height))
    }

    fun convertDpToPx(value: Int): Int {
        return TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            value.toFloat(),
            resources.displayMetrics
        ).toInt()
    }

    fun convertPxToDp(value: Int): Int {
        var density = context.resources.displayMetrics.density
        if (density.toDouble() == 1.0)
            density *= 4.0.toFloat() else if (density.toDouble() == 1.5)
            density *= (8 / 3).toFloat() else if (density.toDouble() == 2.0)
            density *= 2.0.toFloat()
        return (value / density).toInt()
    }

    fun setLowerAlphaPaint(rec: Rectangle, value: Float): Paint {

        val paint = setPaint(rec)
        paint.alpha = (rec.getAlpha() * value).toInt()
        return paint
    }

    fun setPaint(rec: Rectangle): Paint {
        val paint = Paint()
        paint.apply {
            this.style = Paint.Style.FILL
            this.color = Color.argb(rec.getAlpha(), rec.rgba.r, rec.rgba.g, rec.rgba.b)
        }
        return paint
    }

    fun resizeBitmap(bitmap: Bitmap, size: Size): Bitmap {
        val width = convertDpToPx(size.width) // 축소시킬 너비
        val height = convertDpToPx(size.height) // 축소시킬 높이

        var bmpWidth: Float = bitmap.width.toFloat()
        var bmpHeight: Float = bitmap.height.toFloat()

        if (bmpWidth > width) {
            // 원하는 너비보다 클 경우의 설정
            val mWidth: Float = bmpWidth / 100
            val scale = width / mWidth
            bmpWidth *= scale / 100
            bmpHeight *= scale / 100
        } else if (bmpHeight > height) {
            // 원하는 높이보다 클 경우의 설정
            val mHeight: Float = bmpHeight / 100
            val scale = height / mHeight
            bmpWidth *= scale / 100
            bmpHeight *= scale / 100
        }
        return Bitmap.createScaledBitmap(bitmap, bmpWidth.toInt(), bmpHeight.toInt(), true)
    }

}