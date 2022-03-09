package com.example.kotlin_drawingapp


import android.content.Intent
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.contains
import com.example.kotlin_drawingapp.CanvasContract.Presenter
import com.example.kotlin_drawingapp.data.Picture
import com.example.kotlin_drawingapp.data.Point
import com.example.kotlin_drawingapp.data.Rectangle
import com.example.kotlin_drawingapp.data.repository.LocalTextFileRepository
import com.example.kotlin_drawingapp.databinding.ActivityMainBinding
import com.orhanobut.logger.AndroidLogAdapter
import com.orhanobut.logger.Logger
import java.lang.String

class MainActivity : AppCompatActivity(), CanvasContract.View {
    private lateinit var binding: ActivityMainBinding
    lateinit var canvasPresenter: Presenter
    lateinit var myCanvas: MyCanvas
    lateinit var tempCanvas: TempCanvas
    var canvasSize: Pair<Int, Int> = Pair(0, 0)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        canvasPresenter = CanvasPresenter(this, LocalTextFileRepository)

        loggerInitialize()
        attachCanvas()

        addRectangleButtonListening()
        addImageButtonListening()
        changeColorButtonListening()
        changeAlphaSliderListening()
    }

    private fun attachCanvas() {
        myCanvas = myCanvasInitialize()
        binding.canvasContainer.addView(myCanvas)
    }

    private fun addImageButtonListening() {
        val getResult =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
                if (it.resultCode == RESULT_OK) {
                    val uri: Uri = it.data?.data as Uri
                    canvasPresenter.addImageRectangleWithUri(uri)
                }
            }
        binding.imageAddButton?.setOnClickListener {
            intent = Intent(Intent.ACTION_GET_CONTENT)
            intent.type = "image/*"
            getResult.launch(intent)
        }
    }

    private fun addRectangleButtonListening() {
        binding.rectangleButton.setOnClickListener {
            canvasPresenter.addRectangle()
        }
    }

    private fun changeColorButtonListening() {
        binding.rectangleColorButton?.setOnClickListener {
            canvasPresenter.changeRectangleColor()
        }
    }

    private fun changeAlphaSliderListening() {
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

    override fun showImages(pictureList: MutableList<Picture>) {
        myCanvas.drawImage(pictureList)
    }

    override fun showAll(
        rectangleList: MutableList<Rectangle>,
        pictureList: MutableList<Picture>,
        selectedRecList: MutableList<Rectangle>
    ) {
        binding.canvasContainer.removeView(myCanvas)
        myCanvas = myCanvasInitialize()
        myCanvas.drawAll(rectangleList, pictureList, selectedRecList)
        binding.canvasContainer.addView(myCanvas)
    }

    override fun showSelectedColor(colorText: kotlin.String) {
        binding.rectangleColorButton?.text = colorText
    }

    override fun showSelectedAlpha(selectedRec: Rectangle?) {
        selectedRec?.let {
            binding.rectangleAlphaSlider?.value = it.rgba.a.ordinal.toFloat() + 1
        }
    }

    override fun getWindowSize(): Pair<Int, Int> {
        return canvasSize
    }

    private fun myCanvasInitialize(): MyCanvas {
        val canvasSizeListener = object : CanvasSizeListener {
            override fun onMeasure(x: Int, y: Int) {
                canvasSize = Pair(x, y)
            }
        }
        val canvasTouchListener = object : CanvasTouchListener {
            override fun onTouchDown(dpX: Int, dpY: Int) {
                canvasPresenter.setSelectedRectangle(dpX, dpY)
                tempCanvas =
                    TempCanvas(applicationContext, canvasPresenter.getSelectedRectangle(), dpX, dpY)
                binding.canvasContainer.addView(tempCanvas)
            }

            override fun onMove(x: Int, y: Int) {
                canvasPresenter.getSelectedPicture()?.let { tempCanvas.drawTempPic(x, y, it) }
                    ?: kotlin.run {
                        tempCanvas.drawTempRec(x, y)
                    }
            }

            override fun onTouchUP(pxX: Int, pxY: Int) {
                binding.canvasContainer.removeView(tempCanvas)
                val tempPos = tempCanvas.getTempPosPx(pxX, pxY)
                val movedPos =
                    Pair(tempCanvas.convertPxToDp(tempPos.x), tempCanvas.convertPxToDp(tempPos.y))
                canvasPresenter.moveRectangle(tempCanvas.rectangle, movedPos.first, movedPos.second)
            }

        }
        return MyCanvas(
            this,
            canvasTouchListener,
            canvasSizeListener
        )
    }

    private fun loggerInitialize() {
        Logger.addLogAdapter(AndroidLogAdapter())
    }
}

interface CanvasTouchListener {
    fun onTouchDown(x: Int, y: Int)
    fun onMove(x: Int, y: Int)
    fun onTouchUP(x: Int, y: Int)
}

interface CanvasSizeListener {
    fun onMeasure(x: Int, y: Int)
}

