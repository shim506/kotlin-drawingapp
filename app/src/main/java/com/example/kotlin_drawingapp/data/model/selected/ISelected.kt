package com.example.kotlin_drawingapp.data.model.selected

import com.example.kotlin_drawingapp.customView.TempCanvas
import com.example.kotlin_drawingapp.data.Rectangle

interface ISelected {
    fun drawSelected(tempCanvas: TempCanvas , x : Int , y : Int)
    fun changeToRandomColor()
    fun getRectangle(): Rectangle
}