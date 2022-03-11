package com.example.kotlin_drawingapp

import android.graphics.Bitmap
import android.net.Uri
import com.example.kotlin_drawingapp.changeAttr.IChangeAttribute
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
        fun addImageRectangleWithUri(uri: Uri)
        fun changeRectangleAttribute(changeAttribute: IChangeAttribute)

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

        fun showSelectedColor(colorText: String)
        fun showSelectedAlpha(selectedRec: Rectangle?)
        fun getWindowSize(): Pair<Int, Int>
        fun showSelectedAttribute(selectedRec: Rectangle?)
    }

}