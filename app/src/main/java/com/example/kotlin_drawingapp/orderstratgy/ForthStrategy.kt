package com.example.kotlin_drawingapp.orderstratgy

import com.example.kotlin_drawingapp.data.CanvasObjectType
import com.example.kotlin_drawingapp.data.Picture
import com.example.kotlin_drawingapp.data.Rectangle
import com.example.kotlin_drawingapp.data.Text
import java.util.logging.Logger

class ForthStrategy(type: CanvasObjectType, rectangle: Rectangle) :
    CanvasObjectOrderStrategy(type, rectangle) {

    override fun changeRectangleOrder(list: MutableList<Rectangle>) {
        val idx = list.indexOf(rectangle)
        list.removeAt(idx)

        // 마지막 원소일 경우
        try {
          com.orhanobut.logger.Logger.d("ss")
            list.add(idx + 1, rectangle)
        } catch (e: IndexOutOfBoundsException) {
            com.orhanobut.logger.Logger.d("dd")
            list.add(idx, rectangle)
        }

    }

    override fun changePictureOrder(list: MutableList<Picture>) {

        val idx = list.map { it.rec }.indexOf(rectangle)
        val picture = list[idx]
        list.removeAt(idx)

        // 마지막 원소일 경우
        try {
            list.add(idx + 1, picture)
        } catch (e: IndexOutOfBoundsException) {
            list.add(idx, picture)
        }
    }

    override fun changeTextOrder(list: MutableList<Text>) {
        val idx = list.map { it.rec }.indexOf(rectangle)
        val text = list[idx]
        list.removeAt(idx)

        // 마지막 원소일 경우
        try {
            list.add(idx + 1, text)
        } catch (e: IndexOutOfBoundsException) {
            list.add(idx, text)
        }
    }
}