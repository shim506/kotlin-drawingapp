package com.example.kotlin_drawingapp

import com.example.kotlin_drawingapp.data.Rectangle

interface CanvasContract {
    interface Present {
        fun addRectangle()
    }

    interface View {
        fun showRectangle(rectangleList: MutableList<Rectangle>)
    }

}