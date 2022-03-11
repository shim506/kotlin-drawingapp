package com.example.kotlin_drawingapp.data.repository

import com.example.kotlin_drawingapp.data.Rectangle

interface RectangleRepository {
    fun loadRectangles(url: String): List<Rectangle>?

    fun saveRectangles(url : String , rectangles: List<Rectangle>)
}