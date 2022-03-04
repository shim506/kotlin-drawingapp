package com.example.kotlin_drawingapp

import android.content.Context
import com.example.kotlin_drawingapp.data.Plane
import com.example.kotlin_drawingapp.data.Rectangle
import com.example.kotlin_drawingapp.data.RectangleFactory
import com.orhanobut.logger.Logger

private var widthDp: Float = 0F
private var heightDp: Float = 0F

class CanvasPresent(private val canvasView: CanvasContract.View) : CanvasContract.Present {
    init {
        setWindowSize()
    }

    override fun addRectangle() {
        val rect = RectangleFactory().createRectangle(widthDp, heightDp)
        Plane.addRectangle(rect, object : PlaneDataAddListener {
            override fun onEvent(rectangleList: MutableList<Rectangle>) {
                canvasView.showRectangle(rectangleList)
            }
        })
    }

    override fun setSelectedRectangle(x: Int, y: Int) {
        Plane.setSelectedRectangle(x, y)
        canvasView.showSelectedColor(Plane.selectedRec)
        canvasView.showSelectedBound(Plane.selectedRecList)
    }

    override fun changeRectangleColor() {
        Plane.selectedRec?.let {
            Plane.changeSelectedRectangleColor()
            canvasView.showRectangle(Plane.rectangleList)
            canvasView.showSelectedColor(Plane.selectedRec)
        } ?: kotlin.run { return }
    }


    private fun setWindowSize() {
        val metrics = (canvasView as Context).resources.displayMetrics
        widthDp = (metrics.widthPixels /
                metrics.density)

        heightDp = (metrics.heightPixels /
                metrics.density)
    }
}

interface PlaneDataAddListener {
    fun onEvent(rectangleList: MutableList<Rectangle>)
}