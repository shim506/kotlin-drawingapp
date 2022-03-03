package com.example.kotlin_drawingapp.data

import com.example.kotlin_drawingapp.PlaneDataAddListener

object Plane {
    val rectangleList = mutableListOf<Rectangle>()
    var selectedRectangle: Rectangle? = null

    fun getRectangleCount(): Int {
        return rectangleList.size
    }

    fun getRectangle(index: Int): Rectangle? {
        if (rectangleList.size - 1 >= index) return rectangleList[index]
        return null
    }

    fun addRectangle(rectangle: Rectangle, addListener: PlaneDataAddListener) {
        rectangleList.add(rectangle)
        addListener.onEvent(rectangleList)
    }

    fun setSelectedRectangle(x: Int, y: Int): Rectangle? {
        selectedRectangle = null

        rectangleList.forEach {
            if (pointInRectangle(it, x, y)) {
                selectedRectangle = it
            }
        }
        return selectedRectangle
    }

    private fun pointInRectangle(rec: Rectangle, x: Int, y: Int): Boolean {
        if (x >= rec.point.x && y >= rec.point.y) {
            if (x <= rec.point.x + rec.size.width && y <= rec.point.y + rec.size.height) {
                return true
            }
        }
        return false
    }

    fun changeRectangleColor(rectangle: Rectangle) {
        rectangle.rgba.r = (1..255).random()
        rectangle.rgba.g = (1..255).random()
        rectangle.rgba.b = (1..255).random()
    }

}