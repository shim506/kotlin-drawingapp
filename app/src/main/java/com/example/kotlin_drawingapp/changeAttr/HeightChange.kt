package com.example.kotlin_drawingapp.changeAttr

import com.example.kotlin_drawingapp.data.Rectangle

class HeightChange(private val value: Int) : IChangeAttribute {
    override fun applyChange(rectangle: Rectangle) {
        if (isPositive(rectangle.size.height, value)) {
            rectangle.size.height += value
        }
    }
}