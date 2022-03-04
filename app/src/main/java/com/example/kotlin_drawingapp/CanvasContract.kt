package com.example.kotlin_drawingapp

import com.example.kotlin_drawingapp.data.Rectangle

interface CanvasContract {
    interface Present {
        fun addRectangle()
        fun setSelectedRectangle(x: Int, y: Int)
        fun changeRectangleColor()
    }

    interface View {
        fun showRectangle(rectangleList: MutableList<Rectangle>)
        fun showSelectedBound(selectedRecList: MutableList<Rectangle>)
        fun showSelectedColor(selectedRec: Rectangle?)
    }

}