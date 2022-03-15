package com.example.kotlin_drawingapp.data.model.selected

import com.example.kotlin_drawingapp.customView.TempCanvas
import com.example.kotlin_drawingapp.data.Rectangle
import com.example.kotlin_drawingapp.data.Text
import kotlinx.coroutines.selects.select

class TextSelected(val selected: Text) : ISelected {

    override fun drawSelected(tempCanvas: TempCanvas, x: Int, y: Int) {
        tempCanvas.drawTempText(x, y, selected)
    }

    override fun changeToRandomColor() {
        // do Nothing for now
    }

    override fun getRectangle(): Rectangle {
        return selected.rec
    }
}