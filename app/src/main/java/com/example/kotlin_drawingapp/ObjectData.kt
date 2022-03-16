package com.example.kotlin_drawingapp

import com.example.kotlin_drawingapp.data.ObjectType
import com.example.kotlin_drawingapp.data.Rectangle

const val RECTANGLE_OBJECT_TYPE = 1 // rectangle
const val PICTURE_OBJECT_TYPE = 2 // picture
const val TEXT_OBJECT_TYPE = 3 // text

data class ObjectData(
    val type: Int,
    val rectangle: Rectangle,
    val dataNumber: Int
) {
}