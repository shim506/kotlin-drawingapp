package com.example.kotlin_drawingapp

import android.graphics.Bitmap
import android.util.Log
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
                canvasView.showAll(Plane.rectangleList , Plane.pictureList , Plane.selectedRecList)
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
                   canvasView.showAll(Plane.rectangleList , Plane.pictureList , Plane.selectedRecList)
                }
            })
    }

    override fun getSelectedRectangle(): Rectangle? {
        return Plane.selectedRec
    }

    override fun getSelectedPicture(): Picture? {
        return Plane.selectedPicture
    }

    override fun moveRectangle(rectangle: Rectangle?, x: Int, y: Int) {
        Plane.moveRectangle(rectangle, x, y)
        canvasView.showAll(Plane.rectangleList , Plane.pictureList , Plane.selectedRecList)
    }

    override fun setSelectedRectangle(x: Int, y: Int) {
        Plane.setSelectedRectangle(x, y)
        Plane.selectedPicture ?: canvasView.showSelectedColor(Plane.selectedRec)
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

    private fun createImageRectangle(): Rectangle {
        val rec = createRectangle()
        rec.rgba.r = -1
        rec.rgba.g = -1
        rec.rgba.b = -1
        return rec
    }

}

interface PlaneRectangleAddListener {
    fun onEvent(rectangleList: MutableList<Rectangle>)
}

interface PlaneImageAddListener {
    fun onEvent(pictureList: MutableList<Picture>)
}