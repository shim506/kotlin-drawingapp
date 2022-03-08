package com.example.kotlin_drawingapp.data.model

import com.example.kotlin_drawingapp.PlaneImageAddListener
import com.example.kotlin_drawingapp.PlaneRectangleAddListener
import com.example.kotlin_drawingapp.data.Rectangle
import com.example.kotlin_drawingapp.data.RectangleFactory

object Plane {
    var rectangleList = mutableListOf<Rectangle>()
    var selectedRecList = mutableListOf<Rectangle>()
    var selectedRec: Rectangle? = null
    val pictureList = mutableListOf<com.example.kotlin_drawingapp.data.Picture>()

    fun getRectangleCount(): Int {
        return rectangleList.size
    }

    fun getRectangle(index: Int): Rectangle? {
        if (rectangleList.size - 1 >= index) return rectangleList[index]
        return null
    }

    fun addRectangle(rectangle: Rectangle, addListener: PlaneRectangleAddListener) {
        rectangleList.add(rectangle)
        addListener.onEvent(rectangleList)
    }

    fun addImageRectangle(
        picture: com.example.kotlin_drawingapp.data.Picture,
        addListener: PlaneImageAddListener
    ) {
        pictureList.add(picture)
        addListener.onEvent(pictureList)
    }

    // add가 될 수도 있고 빈공간일 경우 모든 데이터를 지우기에 set prefix
    fun setSelectedRectangle(x: Int, y: Int) {
        val allRectangleList = mutableListOf<Rectangle>()
        allRectangleList.addAll(rectangleList)
        allRectangleList.addAll(pictureList.map { it.rec })

        if (existRecOrPic(x, y, allRectangleList)) {
            selectedRecList.clear()
            selectedRec = null
        } else {
            allRectangleList.forEach {
                if (pointInRectangle(it, x, y)) {
                    selectedRecList.add(it)
                    selectedRec = it
                    return
                }
            }
        }
    }

    private fun existRecOrPic(x: Int, y: Int, allList: MutableList<Rectangle>): Boolean {
        return allList.none { pointInRectangle(it, x, y) }
    }

    private fun pointInRectangle(rec: Rectangle, x: Int, y: Int): Boolean {
        if (x >= rec.point.x && y >= rec.point.y) {
            if (x <= rec.point.x + rec.size.width && y <= rec.point.y + rec.size.height) {
                return true
            }
        }
        return false
    }

    fun changeSelectedRectangleColor() {
        selectedRec?.rgba = RectangleFactory().getRandomRgba()
    }


}