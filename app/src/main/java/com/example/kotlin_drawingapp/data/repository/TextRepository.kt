package com.example.kotlin_drawingapp.data.repository

interface TextRepository {
    fun loadText(url: String): String?
}