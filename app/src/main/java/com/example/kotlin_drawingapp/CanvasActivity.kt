package com.example.kotlin_drawingapp


import android.content.Context
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.kotlin_drawingapp.CanvasContract.Presenter
import com.example.kotlin_drawingapp.data.Rectangle
import com.example.kotlin_drawingapp.databinding.ActivityMainBinding
import com.orhanobut.logger.AndroidLogAdapter
import com.orhanobut.logger.Logger
import java.lang.String

class MainActivity : AppCompatActivity(), CanvasContract.View {
    private lateinit var binding: ActivityMainBinding
    lateinit var canvasPresenter: Presenter
    lateinit var myCanvas: MyCanvas
    var canvasSize: Pair<Int, Int> = Pair(0, 0)


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        canvasPresenter = CanvasPresenter(this)

        loggerInitialize()
        addRectangleButtonListening()
        changeColorButtonListening()
        changeAlphaSliderListening()
    }

    private fun addRectangleButtonListening() {
        binding.rectangleButton.setOnClickListener {
            myCanvas = myCanvasInitialize()
            binding.canvasContainer.addView(myCanvas)
            canvasPresenter.addRectangle()
        }
    }

    private fun changeColorButtonListening() {
        binding.rectangleColorButton?.setOnClickListener {
            canvasPresenter.changeRectangleColor()
        }
    }

    override fun changeAlphaSliderListening() {
        binding.rectangleAlphaSlider?.addOnChangeListener { slider, value, fromUser ->
            canvasPresenter.changeRectangleAlpha(value)
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
            binding.rectangleAlphaSlider?.value = it.rgba.a.ordinal.toFloat() + 1
        }
    }

    override fun getWindowSize(): Pair<Int, Int> {
//        val metrics = this.resources.displayMetrics
//        val widthDp = (metrics.widthPixels /
//                metrics.density)
//
//        val heightDp = (metrics.heightPixels /
//                metrics.density)
//        return Pair(widthDp.toInt(), heightDp.toInt())
        return canvasSize
    }

    private fun myCanvasInitialize(): MyCanvas {
        val canvasTouchListener = object : CanvasTouchListener {
            override fun onTouch(x: Int, y: Int) {
                canvasPresenter.setSelectedRectangle(x, y)
            }
        }
        val canvasSizeListener = object : CanvasSizeListener {
            override fun onMeasure(x: Int, y: Int) {
                canvasSize = Pair(x, y)
            }
        }
        return MyCanvas(this, canvasTouchListener, canvasSizeListener)
    }

    private fun loggerInitialize() {
        Logger.addLogAdapter(AndroidLogAdapter())
    }
}

interface CanvasTouchListener {
    fun onTouch(x: Int, y: Int)
}

interface CanvasSizeListener {
    fun onMeasure(x: Int, y: Int)
}