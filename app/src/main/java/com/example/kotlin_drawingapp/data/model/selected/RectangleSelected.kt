package com.example.kotlin_drawingapp.data.model.selected

import com.example.kotlin_drawingapp.customView.TempCanvas
import com.example.kotlin_drawingapp.data.Rectangle
import com.example.kotlin_drawingapp.data.model.Plane

class RectangleSelected(val selected: Rectangle) : ISelected {

    override fun drawSelected(tempCanvas: TempCanvas, x: Int, y: Int) {
        tempCanvas.drawTempRectangle(x, y)
    }

    override fun changeToRandomColor() {
        selected.rgba = Rectangle.getRandomRgba()
    }

    override fun getRectangle(): Rectangle {
        return selected
    }
}