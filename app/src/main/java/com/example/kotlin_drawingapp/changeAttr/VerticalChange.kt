package com.example.kotlin_drawingapp.changeAttr

import com.example.kotlin_drawingapp.data.Rectangle

class VerticalChange(private val value: Int) : IChangeAttribute {
    override fun applyChange(rectangle: Rectangle) {
        if (isPositive(rectangle.point.y, value)) rectangle.point.y += value
    }
}