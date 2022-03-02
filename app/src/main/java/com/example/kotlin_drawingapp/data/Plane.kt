package com.example.kotlin_drawingapp.data

import com.example.kotlin_drawingapp.PlaneDataAddListener

object Plane {
    private val rectangleList = mutableListOf<Rectangle>()

    fun getRectangleCount(): Int {
        return rectangleList.size
    }

    fun getRectangle(index: Int): Rectangle? {
        if (rectangleList.size - 1 >= index) return rectangleList[index]
        return null
    }

    fun addRectangle(rectangle: Rectangle , addListener: PlaneDataAddListener) {
        rectangleList.add(rectangle)
        addListener.onEvent(rectangleList)

    }

}