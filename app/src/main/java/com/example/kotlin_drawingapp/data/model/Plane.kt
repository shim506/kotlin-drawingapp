package com.example.kotlin_drawingapp.data.model

import com.example.kotlin_drawingapp.PlaneImageAddListener
import com.example.kotlin_drawingapp.PlaneRectangleAddListener
import com.example.kotlin_drawingapp.data.*
import com.orhanobut.logger.Logger
import com.orhanobut.logger.Logger.d

object Plane {
    var rectangleList = mutableListOf<Rectangle>()
    var selectedRecList = mutableListOf<Rectangle>()
    var selectedRec: Rectangle? = null
    var selectedPicture: Picture? = null
    var selectedText : Text? = null
    val pictureList = mutableListOf<com.example.kotlin_drawingapp.data.Picture>()
    val textList = mutableListOf<Text>()

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
        Logger.d(rectangleList)
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
    //    allRectangleList.addAll(textList.map { it.rec })

        if (existRecOrPic(x, y, allRectangleList)) {
            selectedRecList.clear()
            selectedRec = null
        } else {
            rectangleList.forEach {
                if (pointInRectangle(it, x, y)) {
                    Logger.d("sdddd")
                    selectedRecList.add(it)
                    selectedRec = it
                    selectedPicture = null
                    return
                }
            }
            pictureList.forEach {
                if (pointInRectangle(it.rec, x, y)) {
                    selectedRecList.add(it.rec)
                    selectedRec = it.rec
                    selectedPicture = it
                    return
                }
            }
//            textList.forEach {
//                if (pointInRectangle(it.rec, x, y)) {
//                    selectedRecList.add(it.rec)
//                    selectedRec = it.rec
//                    selectedText = it
//                    return
//                }
//            }
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
        selectedRec?.rgba = Rectangle.getRandomRgba()
    }

    fun moveRectangle(rectangle: Rectangle?, x: Int, y: Int) {
        rectangle?.point = Point(x, y)
    }

    fun addText(randomText: Text) {
        textList.add(randomText)
    }


}