package com.example.kotlin_drawingapp

import com.example.kotlin_drawingapp.data.Rectangle

interface CanvasContract {
    interface Present {
        fun addRectangle()
        fun setSelectedRectangle(x: Int, y: Int)
    }

    interface View {
        fun showRectangle(rectangleList: MutableList<Rectangle>)
        fun showSelectedBound(selectedRec: MutableList<Rectangle>)
    }

}