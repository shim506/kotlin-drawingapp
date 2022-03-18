package com.example.kotlin_drawingapp.orderstratgy

import com.example.kotlin_drawingapp.data.*

abstract class CanvasObjectOrderStrategy(
    private val objectType: CanvasObjectType,
    val rectangle: Rectangle
) {
    fun changeOrder(
        rectangleList: MutableList<Rectangle>,
        pictureList: MutableList<Picture>,
        textList: MutableList<Text>
    ) {
        when (objectType) {
            CanvasObjectType.RECTANGLE -> {
                changeRectangleOrder(rectangleList)
            }
            CanvasObjectType.PICTURE -> {
                changePictureOrder(pictureList)
            }
            CanvasObjectType.TEXT -> {
                changeTextOrder(textList)
            }
        }
    }

    protected abstract fun changeRectangleOrder(list: MutableList<Rectangle>)
    protected abstract fun changePictureOrder(list: MutableList<Picture>)
    protected abstract fun changeTextOrder(list: MutableList<Text>)
}