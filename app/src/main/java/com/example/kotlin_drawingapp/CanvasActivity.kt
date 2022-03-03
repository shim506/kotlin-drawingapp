package com.example.kotlin_drawingapp


import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

import com.example.kotlin_drawingapp.CanvasContract.Present
import com.example.kotlin_drawingapp.data.MyCanvas
import com.example.kotlin_drawingapp.data.Rectangle
import com.example.kotlin_drawingapp.databinding.ActivityMainBinding
import com.orhanobut.logger.AndroidLogAdapter
import com.orhanobut.logger.Logger


class MainActivity : AppCompatActivity(), CanvasContract.View {
    private lateinit var binding: ActivityMainBinding
    lateinit var canvasPresent: Present
    lateinit var myCanvas: MyCanvas

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
            myCanvas = MyCanvas(this)
            binding.canvasContainer?.addView(myCanvas)
            canvasPresent.addRectangle()
        }
    }

    override fun showRectangle(rectangleList: MutableList<Rectangle>) {
        rectangleList.forEach {
            Logger.d(it.id)
            myCanvas.drawRectangle(it)
        }
    }

    private fun loggerInitialize() {
        Logger.addLogAdapter(AndroidLogAdapter())
    }

}