package com.example.kotlin_drawingapp.data

import android.graphics.Bitmap
import android.graphics.Picture

import com.example.kotlin_drawingapp.data.Point

const val PICTURE_WIDTH = 210
const val PICTURE_HEIGHT = 210

class Picture(
    val byteArray: ByteArray,
    val box: Box
)