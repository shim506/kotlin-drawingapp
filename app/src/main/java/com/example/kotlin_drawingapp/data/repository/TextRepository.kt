package com.example.kotlin_drawingapp.data.repository

import com.example.kotlin_drawingapp.data.Rectangle

interface TextRepository {
    fun loadText(url: String): String?
}