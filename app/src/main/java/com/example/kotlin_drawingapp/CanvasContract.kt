package com.example.kotlin_drawingapp

import android.graphics.Bitmap
import com.example.kotlin_drawingapp.data.Picture
import com.example.kotlin_drawingapp.data.Rectangle

interface CanvasContract {
    interface Presenter {
        fun addRectangle()
        fun setSelectedRectangle(x: Int, y: Int)
        fun changeRectangleColor()
        fun changeRectangleAlpha(value: Float)
        fun addImageRectangle(bitmap: Bitmap)
        fun getSelectedRectangle(): Rectangle?
        fun getSelectedPicture(): Picture?
        fun moveRectangle(rectangle: Rectangle?, x: Int, y: Int)
    }

    interface View {
        fun showRectangle(rectangleList: MutableList<Rectangle>)
        fun showImages(pictureList: MutableList<Picture>)
        fun showSelectedBound(selectedRecList: MutableList<Rectangle>)
        fun showAll(
            rectangleList: MutableList<Rectangle>,
            pictureList: MutableList<Picture>,
            selectedRecList: MutableList<Rectangle>
        )
        fun showSelectedColor(selectedRec: Rectangle?)
        fun showSelectedAlpha(selectedRec: Rectangle?)
        fun getWindowSize(): Pair<Int, Int>
    }

}