package com.example.kotlin_drawingapp.data

import android.graphics.Rect
import com.example.kotlin_drawingapp.PlaneDataAddListener

object Plane {
    val rectangleList = mutableListOf<Rectangle>()
    var selectedRecList = mutableListOf<Rectangle>()
    val selectedRec: Rectangle? = null

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

    // add가 될 수도 있고 빈공간일 경우 모든 데이터를 지우기에 set prefix
    fun setSelectedRectangle(x: Int, y: Int) {
        rectangleList.forEach {
            if (pointInRectangle(it, x, y)) {
                selectedRecList.add(it)
            }
        }
        if (rectangleList.none { pointInRectangle(it, x, y) }) selectedRecList.clear()
    }

    private fun pointInRectangle(rec: Rectangle, x: Int, y: Int): Boolean {
        if (x >= rec.point.x && y >= rec.point.y) {
            if (x <= rec.point.x + rec.size.width && y <= rec.point.y + rec.size.height) {
                return true
            }
        }
        return false
    }


}