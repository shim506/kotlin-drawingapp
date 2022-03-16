package com.example.kotlin_drawingapp.data

data class Text(val text: String, val rec: Rectangle) : Object {
    var textNumber = -1

    init {
        accumulateTextNum++
        textNumber = accumulateTextNum
    }

    companion object {
        var accumulateTextNum = 0
    }

    override fun getNumber(): Int {
        return textNumber
    }
}

