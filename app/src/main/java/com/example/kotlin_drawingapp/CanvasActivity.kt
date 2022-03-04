package com.example.kotlin_drawingapp


import android.graphics.Color
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

import com.example.kotlin_drawingapp.CanvasContract.Present
import com.example.kotlin_drawingapp.data.MyCanvas
import com.example.kotlin_drawingapp.data.Plane
import com.example.kotlin_drawingapp.data.Rectangle
import com.example.kotlin_drawingapp.databinding.ActivityMainBinding
import com.orhanobut.logger.AndroidLogAdapter
import com.orhanobut.logger.Logger
import java.lang.String


class MainActivity : AppCompatActivity(), CanvasContract.View {
    private lateinit var binding: ActivityMainBinding
    lateinit var canvasPresent: Present
    lateinit var myCanvas: MyCanvas
    var selectedRectangle: Rectangle? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        canvasPresent = CanvasPresent(this)

        loggerInitialize()
        addRectangleButtonListening()
    }

    private fun addRectangleButtonListening() {
        binding.rectangleButton.setOnClickListener {
            myCanvas = myCanvasInitialize()
            binding.canvasContainer?.addView(myCanvas)
            canvasPresent.addRectangle()
        }
    }

    override fun showSelectedBound(selectedRec: MutableList<Rectangle>) {
        myCanvas.drawBound(selectedRec)
    }

    override fun showRectangle(rectangleList: MutableList<Rectangle>) {
        myCanvas.drawRectangle(rectangleList)
    }

    private fun myCanvasInitialize(): MyCanvas {
        return MyCanvas(this, selectedRectangle, object : CanvasTouchListener {
            override fun onTouch(x: Int, y: Int) {
                canvasPresent.setSelectedRectangle(x, y)
            }
        })
    }

    private fun loggerInitialize() {
        Logger.addLogAdapter(AndroidLogAdapter())
    }
}

interface CanvasTouchListener {
    fun onTouch(x: Int, y: Int)
}