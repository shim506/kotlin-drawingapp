package com.example.kotlin_drawingapp

import android.app.Activity
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import com.example.kotlin_drawingapp.changeAttr.IChangeAttribute
import com.example.kotlin_drawingapp.changeAttr.PositiveLimitException
import com.example.kotlin_drawingapp.data.*
import com.example.kotlin_drawingapp.data.model.Plane
import com.example.kotlin_drawingapp.data.repository.RectangleRepository
import com.orhanobut.logger.Logger
import java.io.ByteArrayOutputStream

class CanvasPresenter(
    private val canvasView: CanvasContract.View,
    private val repository: RectangleRepository
) : CanvasContract.Presenter {
    init {
        repository.loadRectangles("url")?.let { Plane.rectangleList = it.toMutableList() }
    }

    override fun addRectangle() {
        val rect = createRectangle()
        Plane.addRectangle(rect, object : PlaneRectangleAddListener {
            override fun onEvent(rectangleList: MutableList<Rectangle>) {
                canvasView.showAll(Plane.rectangleList, Plane.pictureList, Plane.selectedRecList)
            }
        })
    }

    override fun addImageRectangle(bitmap: Bitmap) {
        val stream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream)
        Plane.addImageRectangle(
            Picture(stream.toByteArray(), createImageRectangle()),
            object : PlaneImageAddListener {
                override fun onEvent(pictureList: MutableList<Picture>) {
                    canvasView.showAll(
                        Plane.rectangleList,
                        Plane.pictureList,
                        Plane.selectedRecList
                    )
                }
            })
    }

    override fun addImageRectangleWithUri(uri: Uri) {
        val bitmap = getBitmapWithUri(uri)
        addImageRectangle(bitmap)
    }

    override fun getSelectedRectangle(): Rectangle? {
        return Plane.selectedRec
    }

    override fun getSelectedPicture(): Picture? {
        return Plane.selectedPicture
    }

    override fun moveRectangle(rectangle: Rectangle?, x: Int, y: Int) {
        Plane.moveRectangle(rectangle, x, y)
        canvasView.showAll(Plane.rectangleList, Plane.pictureList, Plane.selectedRecList)
    }

    override fun changeRectangleAttribute(changeAttribute: IChangeAttribute) {
        try {
            Plane.selectedRec?.let { changeAttribute.applyChange(it) }
            canvasView.showAll(Plane.rectangleList, Plane.pictureList, Plane.selectedRecList)
            canvasView.showSelectedAttribute(Plane.selectedRec)
        } catch (e: PositiveLimitException) {
            Toast.makeText(canvasView as MainActivity, "1 미만의 값을 가질 수 없습니다", Toast.LENGTH_SHORT).show()
        }
    }

    override fun setSelectedRectangle(x: Int, y: Int) {
        Plane.setSelectedRectangle(x, y)
        val colorText: String = getSelectedColor(Plane.selectedRec)
        canvasView.showSelectedColor(colorText)
        canvasView.showSelectedAlpha(Plane.selectedRec)
        canvasView.showSelectedBound(Plane.selectedRecList)
        canvasView.showSelectedAttribute(Plane.selectedRec)
    }

    private fun getSelectedColor(selectedRec: Rectangle?): String {
        val colorText =
            selectedRec?.let {
                with(selectedRec.rgba) {
                    if (this.r != -1) {
                        java.lang.String.format("#%02X%02X%02X", this.r, this.g, this.b)
                    } else "null"
                }
            } ?: "null"
        return colorText
    }

    override fun changeRectangleColor() {
        Plane.selectedRec?.let {
            Plane.changeSelectedRectangleColor()
            canvasView.showRectangle(Plane.rectangleList)
            canvasView.showSelectedColor(getSelectedColor(Plane.selectedRec))
        } ?: kotlin.run { return }
    }

    override fun changeRectangleAlpha(value: Float) {
        Plane.selectedRec?.let {
            val alphaList = AlphaEnum.values()
            it.rgba.a = alphaList[(value.toInt() - 1)]
            canvasView.showRectangle(Plane.rectangleList)
            canvasView.showSelectedColor(getSelectedColor(Plane.selectedRec))
        } ?: kotlin.run { return }
    }

    private fun createRectangle(): Rectangle {
        val (widthDP, heightDP) = canvasView.getWindowSize()
        return RectangleFactory().createRectangle(widthDP.toFloat(), heightDP.toFloat())
    }

    private fun createImageRectangle(): Rectangle {
        val rec = createRectangle()
        rec.rgba.r = -1
        rec.rgba.g = -1
        rec.rgba.b = -1
        return rec
    }

    private fun getBitmapWithUri(uri: Uri): Bitmap {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            val source = ImageDecoder.createSource((canvasView as Activity).contentResolver, uri)
            ImageDecoder.decodeBitmap(source)
        } else {
            MediaStore.Images.Media.getBitmap((canvasView as Activity).contentResolver, uri)
        }
    }
}

interface PlaneRectangleAddListener {
    fun onEvent(rectangleList: MutableList<Rectangle>)
}

interface PlaneImageAddListener {
    fun onEvent(pictureList: MutableList<Picture>)
}