package com.example.kotlin_drawingapp

import android.graphics.Bitmap
import com.example.kotlin_drawingapp.data.AlphaEnum
import com.example.kotlin_drawingapp.data.Picture
import com.example.kotlin_drawingapp.data.model.Plane
import com.example.kotlin_drawingapp.data.Rectangle
import com.example.kotlin_drawingapp.data.RectangleFactory
import com.example.kotlin_drawingapp.data.repository.RectangleRepository
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
                canvasView.showRectangle(rectangleList)
            }
        })
    }

    override fun addImageRectangle(bitmap: Bitmap) {
        val stream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream)
        Plane.addImageRectangle(Picture(stream.toByteArray(), createRectangle()) , object :PlaneImageAddListener{
            override fun onEvent(pictureList: MutableList<Picture>) {
                canvasView.showImages(pictureList)
            }
        })
    }

    override fun setSelectedRectangle(x: Int, y: Int) {
        Plane.setSelectedRectangle(x, y)
        canvasView.showSelectedColor(Plane.selectedRec)
        canvasView.showSelectedAlpha(Plane.selectedRec)
        canvasView.showSelectedBound(Plane.selectedRecList)
    }

    override fun changeRectangleColor() {
        Plane.selectedRec?.let {
            Plane.changeSelectedRectangleColor()
            canvasView.showRectangle(Plane.rectangleList)
            canvasView.showSelectedColor(Plane.selectedRec)
        } ?: kotlin.run { return }
    }

    override fun changeRectangleAlpha(value: Float) {
        Plane.selectedRec?.let {
            val alphaList = AlphaEnum.values()
            it.rgba.a = alphaList[(value.toInt() - 1)]
            canvasView.showRectangle(Plane.rectangleList)
            canvasView.showSelectedColor(Plane.selectedRec)
        } ?: kotlin.run { return }
    }

    private fun createRectangle(): Rectangle {
        val (widthDP, heightDP) = canvasView.getWindowSize()
        return RectangleFactory().createRectangle(widthDP.toFloat(), heightDP.toFloat())
    }
}

interface PlaneRectangleAddListener {
    fun onEvent(rectangleList: MutableList<Rectangle>)
}

interface PlaneImageAddListener {
    fun onEvent(pictureList: MutableList<Picture>)
}