package com.example.kotlin_drawingapp.data.repository

import com.example.kotlin_drawingapp.data.Rectangle

object LocalRectangleRepository : RectangleRepository {

    override fun loadRectangles(url: String): List<Rectangle>? {
        // url 을 통해 data 를 읽어 오는 코드
        return null
    }

    override fun saveRectangles(url: String, rectangles: List<Rectangle>) {
        //
    }
}