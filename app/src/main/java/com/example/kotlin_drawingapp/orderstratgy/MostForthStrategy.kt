package com.example.kotlin_drawingapp.orderstratgy

import com.example.kotlin_drawingapp.data.CanvasObjectType
import com.example.kotlin_drawingapp.data.Picture
import com.example.kotlin_drawingapp.data.Rectangle
import com.example.kotlin_drawingapp.data.Text

class MostForthStrategy(type: CanvasObjectType, rectangle: Rectangle) :
    CanvasObjectOrderStrategy(type, rectangle) {


    override fun changeRectangleOrder(list: MutableList<Rectangle>) {
        list.remove(rectangle)
        list.add(list.size, rectangle)
    }

    override fun changePictureOrder(list: MutableList<Picture>) {
        val idx = list.map { it.rec }.indexOf(rectangle)
        val picture = list[idx]
        list.removeAt(idx)
        list.add(list.size, picture)
    }

    override fun changeTextOrder(list: MutableList<Text>) {
        val idx = list.map { it.rec }.indexOf(rectangle)
        val text = list[idx]
        list.removeAt(idx)
        list.add(list.size, text)
    }
}