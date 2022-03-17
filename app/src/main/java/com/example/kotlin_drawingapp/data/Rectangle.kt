package com.example.kotlin_drawingapp.data

import java.lang.StringBuilder

const val RECTANGLE_WIDTH = 150
const val RECTANGLE_HEIGHT = 120
const val RGB_MIN_VALUE = 0
const val RGB_MAX_VALUE = 255
const val ALPHA_MIN_VALUE = 1
const val ALPHA_MAX_VALUE = 10

class Rectangle private constructor() : Object {
    var num = -1
    lateinit var id: String
    lateinit var size: Size
    lateinit var point: Point
    lateinit var rgba: Rgba

    override fun getNumber(): Int {
        return num
    }

    override fun toString(): String {
        return "Rect$num ($id), X:${point.x},Y:${point.y}, W${size.width}, H${size.height}, R:${rgba.r}, G:${rgba.g}, B:${rgba.b}, Alpha:${rgba.a}"
    }

    fun getAlpha(): Int {
        return (rgba.a.ordinal + 1) * 255 / 10
    }

    companion object Factory {
        private var rectangleNum = 0

        fun createRectangle(maxWidth: Float, maxHeight: Float): Rectangle {
            rectangleNum++
            val rectangle = Rectangle()
            rectangle.id = getRandomId()
            rectangle.size = Size(RECTANGLE_WIDTH, RECTANGLE_HEIGHT)
            rectangle.point = getRandomPoint(maxWidth.toInt(), maxHeight.toInt())
            rectangle.rgba = getRandomRgba()
            rectangle.num = rectangleNum
            return rectangle
        }

        fun createRectangleForOther(maxWidth: Float, maxHeight: Float): Rectangle {
            val rectangle = Rectangle()
            rectangle.id = getRandomId()
            rectangle.size = Size(RECTANGLE_WIDTH, RECTANGLE_HEIGHT)
            rectangle.point = getRandomPoint(maxWidth.toInt(), maxHeight.toInt())
            rectangle.rgba = getRandomRgba()
            rectangle.num = -1
            return rectangle
        }

        fun getRandomRgba(): Rgba {
            val r = (RGB_MIN_VALUE..RGB_MAX_VALUE).random()
            val g = (RGB_MIN_VALUE..RGB_MAX_VALUE).random()
            val b = (RGB_MIN_VALUE..RGB_MAX_VALUE).random()
            val a = (ALPHA_MIN_VALUE..ALPHA_MAX_VALUE).random()

            val enumList = AlphaEnum.values()
            return Rgba(r, g, b, enumList[a - 1])
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
            st.setLength(st.length - 1);
            return st.toString()
        }
    }
}

data class Size(var width: Int, var height: Int)
data class Point(var x: Int, var y: Int)
data class Rgba(var r: Int, var g: Int, var b: Int, var a: AlphaEnum)

enum class AlphaEnum {
    ONE, TWO, THREE, FOUR, FIVE, SIX, SEVEN, EIGHT, NINE, Ten
}
