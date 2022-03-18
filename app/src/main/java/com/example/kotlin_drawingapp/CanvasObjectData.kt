package com.example.kotlin_drawingapp

import com.example.kotlin_drawingapp.data.CanvasObjectType
import com.example.kotlin_drawingapp.data.Rectangle


data class CanvasObjectData(
    val type: CanvasObjectType,
    val rectangle: Rectangle,
    val dataNumber: Int
) {
}