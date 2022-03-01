package com.example.kotlin_drawingapp.data

import java.lang.StringBuilder


const val RECTANGLE_WIDTH = 150
const val RECTANGLE_HEIGHT = 120
const val RGB_MIN_VALUE = 0
const val RGB_MAX_VALUE = 255
const val ALPHA_MIN_VALUE = 0
const val ALPHA_MAX_VALUE = 10

class Rectangle() {
    var num = -1
    lateinit var id: String
    lateinit var size: Size
    lateinit var point: Point
    lateinit var rgba: Rgba

    override fun toString(): String {

        return "d"
    }
}

data class Size(val width: Int, val height: Int)
data class Point(val x: Int, val y: Int)
data class Rgba(val r: Int, val g: Int, val b: Int, val a: Int)


class RectangleFactory() {
    companion object {
        var rectangleNum = 0
    }

    fun createRectangle(maxWidth: Int, maxHeight: Int): Rectangle {
        rectangleNum++
        val rectangle = Rectangle()
        rectangle.id = getRandomId()
        rectangle.size = Size(RECTANGLE_WIDTH, RECTANGLE_HEIGHT)
        rectangle.point = getRandomPoint(maxWidth, maxHeight)
        rectangle.rgba = getRandomRgba()
        rectangle.num = rectangleNum
        return rectangle
    }

    private fun getRandomRgba(): Rgba {
        val r = (RGB_MIN_VALUE..RGB_MAX_VALUE).random()
        val g = (RGB_MIN_VALUE..RGB_MAX_VALUE).random()
        val b = (RGB_MIN_VALUE..RGB_MAX_VALUE).random()
        val a = (ALPHA_MIN_VALUE..ALPHA_MAX_VALUE).random()
        return Rgba(r, g, b, a)
    }

    private fun getRandomPoint(maxWidth: Int, maxHeight: Int): Point {
        val width = (0..maxWidth).random()
        val height = (0..maxHeight).random()
        return Point(width, height)
    }

    fun getRandomId(): String {
        val st = StringBuilder()
        st.append((97..122).random().toChar())

        // 두번
        var numericIterValue = (0..2).random()
        repeat(numericIterValue) { st.append((0..9).random()) }
        repeat(2 - numericIterValue) { st.append((97..122).random().toChar()) }
        st.append('-')


        repeat(2) {
            // 세번
            numericIterValue = (0..3).random()
            repeat(numericIterValue) { st.append((0..9).random()) }
            repeat(3 - numericIterValue) { st.append((97..122).random().toChar()) }
            st.append('-')
        }


        return st.toString()
    }

}