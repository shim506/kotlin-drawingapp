package com.example.kotlin_drawingapp.changeAttr

import com.example.kotlin_drawingapp.data.Rectangle

class HorizontalChange(private val value: Int) : IChangeAttribute {
    override fun applyChange(rectangle: Rectangle) {
        if (isPositive(rectangle.point.x, value)) rectangle.point.x += value
    }

}