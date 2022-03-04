package com.example.kotlin_drawingapp


import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

import com.example.kotlin_drawingapp.CanvasContract.Present
import com.example.kotlin_drawingapp.data.Rectangle
import com.example.kotlin_drawingapp.databinding.ActivityMainBinding
import com.orhanobut.logger.AndroidLogAdapter
import com.orhanobut.logger.Logger
import java.lang.String

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
        changeColorButtonListening()
        changeAlphaSliderListening()
    }

    private fun addRectangleButtonListening() {
        binding.rectangleButton.setOnClickListener {
            myCanvas = myCanvasInitialize()
            binding.canvasContainer?.addView(myCanvas)
            canvasPresent.addRectangle()
        }
    }

    private fun changeColorButtonListening() {
        binding.rectangleColorButton?.setOnClickListener {
            canvasPresent.changeRectangleColor()
        }
    }

    override fun changeAlphaSliderListening() {
        binding.rectangleAlphaSlider?.addOnChangeListener { slider, value, fromUser ->
            canvasPresent.changeRectangleAlpha(value)
        }
    }


    override fun showSelectedBound(selectedRec: MutableList<Rectangle>) {
        myCanvas.drawBound(selectedRec)
    }

    override fun showRectangle(rectangleList: MutableList<Rectangle>) {
        myCanvas.drawRectangle(rectangleList)
    }

    override fun showSelectedColor(selectedRec: Rectangle?) {
        val colorText =
            selectedRec?.let { String.format("#%02X%02X%02X", it.rgba.r, it.rgba.g, it.rgba.b) }
                ?: "null"

        binding.rectangleColorButton?.text = colorText
    }

    override fun showSelectedAlpha(selectedRec: Rectangle?) {
        selectedRec?.let {
            binding.rectangleAlphaSlider?.value = it.rgba.a.ordinal.toFloat()+1
        }
    }

    private fun myCanvasInitialize(): MyCanvas {
        return MyCanvas(this, object : CanvasTouchListener {
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