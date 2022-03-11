package com.example.kotlin_drawingapp.changeAttr

import com.example.kotlin_drawingapp.data.Rectangle

class WidthChange(private val value: Int) : IChangeAttribute {

    override fun applyChange(rectangle: Rectangle) {
        if (isPositive(rectangle.size.width, value)) rectangle.size.width += value
    }
}