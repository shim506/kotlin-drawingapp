package com.example.kotlin_drawingapp.data.repository

import java.io.BufferedReader
import java.io.File

object LocalTextRepository : TextRepository {

    val defaultUrl =
        "C:/Users/User/Desktop/CodeSquad/AndroidMission/kotlin-drawingapp/textSource.txt"

    
    override fun loadText(url: String): String? {
        var bufferedReader: BufferedReader? = null
        try {
            bufferedReader = File(url).bufferedReader()
        } catch (e: Exception) {
            bufferedReader = File(defaultUrl).bufferedReader()
        }

        return bufferedReader.use { it?.readText() }
    }
}