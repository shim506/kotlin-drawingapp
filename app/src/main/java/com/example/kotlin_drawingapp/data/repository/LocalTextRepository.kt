package com.example.kotlin_drawingapp.data.repository

import java.io.BufferedReader
import java.io.File

object LocalTextRepository : TextRepository {

    val defaultUrl =
        "C:/Users/User/Desktop/CodeSquad/AndroidMission/kotlin-drawingapp/textSource.txt"

    private val text =
        "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Egestas tellus rutrum tellus pellentesque eu. Viverra justo nec ultrices dui sapien eget mi proin sed. Vel pretium lectus quam id leo. Molestie at elementum eu facilisis sed odio morbi quis commodo. Risus at ultrices mi tempus imperdiet nulla malesuada. In est ante in nibh mauris cursus mattis molestie a. Venenatis urna cursus eget nunc. Eget velit aliquet sagittis id consectetur purus ut. Amet consectetur adipiscing elit pellentesque habitant morbi tristique senectus. Consequat nisl vel pretium lectus quam id. Nisl vel pretium lectus quam id leo in vitae turpis. Purus faucibus ornare suspendisse sed. Amet mauris commodo quis imperdiet."

    override fun loadText(url: String): String? {
        var bufferedReader: BufferedReader? = null
        try {
            bufferedReader = File(url).bufferedReader()
            return bufferedReader.use { it?.readText() }
        } catch (e: Exception) {
            return text
        }
    }
    fun loadText(): String{
        return text
    }
}