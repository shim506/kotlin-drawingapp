package com.example.kotlin_drawingapp.data

import org.junit.Assert.*

import org.junit.Test

class RectangleFactoryTest {
    @Test
    fun getRandomId() {
        repeat(10) {
            val rectangleFactory = RectangleFactory()
            val id = rectangleFactory.getRandomId()
            for (i in id.indices) {
                if ((i + 1) % 4 == 0) {
                    assertEquals('-', id[i])
                } else if (i == 0) {
                    assertEquals(true, id[i].isLowerCase())
                } else {
                    assertEquals(true, id[i].isDigit() || id[i].isLowerCase())
                }
            }
            println(id)
        }
    }
}