package com.example.kotlin_drawingapp.data

data class Picture(
    val byteArray: ByteArray,
    val rec: Rectangle
) : CanvasObject {
    var pictureNumber = -1

    init {
        accumulatePictureNum++
        pictureNumber = accumulatePictureNum
    }

    companion object {
        var accumulatePictureNum = 0
    }

    override fun getNumber(): Int {
        return pictureNumber
    }
}