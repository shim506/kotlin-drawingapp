package com.example.kotlin_drawingapp.data.model.selected

import com.example.kotlin_drawingapp.customView.TempCanvas
import com.example.kotlin_drawingapp.data.Picture
import com.example.kotlin_drawingapp.data.Rectangle

class PictureSelected(val picture: Picture) : ISelected {

    override fun drawSelected(tempCanvas: TempCanvas, x: Int, y: Int) {
        tempCanvas.drawTempPicture(x, y, picture)
    }

    override fun changeToRandomColor() {
    }

    override fun getRectangle(): Rectangle {
        return picture.rec
    }
}