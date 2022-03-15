package com.example.kotlin_drawingapp.changeAttr

import com.example.kotlin_drawingapp.data.Rectangle
import com.orhanobut.logger.Logger

interface IChangeAttribute {
    fun applyChange(rectangle: Rectangle)
    fun isPositive(attrValue: Int, value: Int): Boolean {
        if (attrValue + value < 1) throw PositiveLimitException() else return true
    }
}