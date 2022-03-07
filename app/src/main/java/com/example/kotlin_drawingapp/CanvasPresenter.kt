package com.example.kotlin_drawingapp

import android.content.Context
import com.example.kotlin_drawingapp.data.AlphaEnum
import com.example.kotlin_drawingapp.data.model.Plane
import com.example.kotlin_drawingapp.data.Rectangle
import com.example.kotlin_drawingapp.data.RectangleFactory

class CanvasPresenter(private val canvasView: CanvasContract.View) : CanvasContract.Presenter {

    override fun addRectangle() {
        val (widthDP, heightDP) = canvasView.getWindowSize()
        val rect = RectangleFactory().createRectangle(widthDP.toFloat(), heightDP.toFloat())
        Plane.addRectangle(rect, object : PlaneDataAddListener {
            override fun onEvent(rectangleList: MutableList<Rectangle>) {
                canvasView.showRectangle(rectangleList)
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

}

interface PlaneDataAddListener {
    fun onEvent(rectangleList: MutableList<Rectangle>)
}