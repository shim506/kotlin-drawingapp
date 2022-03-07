package com.example.kotlin_drawingapp

import com.example.kotlin_drawingapp.data.Rectangle

interface CanvasContract {
    interface Presenter {
        fun addRectangle()
        fun setSelectedRectangle(x: Int, y: Int)
        fun changeRectangleColor()
        fun changeRectangleAlpha(value: Float)
    }

    interface View {
        fun showRectangle(rectangleList: MutableList<Rectangle>)
        fun showSelectedBound(selectedRecList: MutableList<Rectangle>)
        fun showSelectedColor(selectedRec: Rectangle?)
        fun changeAlphaSliderListening()
        fun showSelectedAlpha(selectedRec: Rectangle?)
        fun getWindowSize(): Pair<Int, Int>
    }

}