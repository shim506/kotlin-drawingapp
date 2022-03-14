package com.example.kotlin_drawingapp

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.example.kotlin_drawingapp.CanvasContract.Presenter
import com.example.kotlin_drawingapp.changeAttr.HeightChange
import com.example.kotlin_drawingapp.changeAttr.HorizontalChange
import com.example.kotlin_drawingapp.changeAttr.VerticalChange
import com.example.kotlin_drawingapp.changeAttr.WidthChange
import com.example.kotlin_drawingapp.customView.MyCanvas
import com.example.kotlin_drawingapp.customView.TempCanvas
import com.example.kotlin_drawingapp.data.Picture
import com.example.kotlin_drawingapp.data.Point
import com.example.kotlin_drawingapp.data.Rectangle
import com.example.kotlin_drawingapp.data.Size
import com.example.kotlin_drawingapp.data.repository.LocalRectangleRepository
import com.example.kotlin_drawingapp.databinding.ActivityMainBinding
import com.orhanobut.logger.AndroidLogAdapter
import com.orhanobut.logger.Logger

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
        canvasPresenter = CanvasPresenter(this, LocalRectangleRepository)

        loggerInitialize()
        attachCanvas()

        addRectangleButtonListening()
        addImageButtonListening()
        changeColorButtonListening()
        changeAlphaSliderListening()

        attrUpDownButtonInitialize()
        attrUpDownButtonListening()
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

    override fun showSelectedAttribute(selectedRec: Rectangle?) {
        binding.posXUpDownView?.value?.text = (selectedRec?.point?.x).toString()
        binding.posYUpDownView?.value?.text = (selectedRec?.point?.y).toString()
        binding.sizeWidthUpDownView?.value?.text = (selectedRec?.size?.width).toString()
        binding.sizeHeightUpDownView?.value?.text = (selectedRec?.size?.height).toString()
    }

    override fun showSelectedAttribute(point: Point, size: Size?) {
        binding.posXUpDownView?.value?.text = point.x.toString()
        binding.posYUpDownView?.value?.text = point.y.toString()
        binding.sizeWidthUpDownView?.value?.text = size?.width.toString()
        binding.sizeHeightUpDownView?.value?.text = size?.height.toString()
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
                canvasPresenter.getSelectedPicture()?.let { tempCanvas.drawTempPicture(x, y, it) }
                    ?: kotlin.run {
                        tempCanvas.drawTempRectangle(x, y)
                    }
                val (point, size) = tempCanvas.getTempAttrDP(x, y)
                tempAttrUiUpdateDp(point, size)
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


    private fun tempAttrUiUpdateDp(point: Point, size: Size?) {
        showSelectedAttribute(point , size)
    }

    private fun attrUpDownButtonInitialize() {
        binding.posXUpDownView?.attr?.text = "X"
        binding.posYUpDownView?.attr?.text = "Y"
        binding.sizeWidthUpDownView?.attr?.text = "W"
        binding.sizeHeightUpDownView?.attr?.text = "H"
    }


    private fun attrUpDownButtonListening() {
        with(binding) {
            // x 좌표 변화
            posXUpDownView?.upButton?.setOnClickListener {
                canvasPresenter.changeRectangleAttribute(HorizontalChange(1))
            }
            posXUpDownView?.downButton?.setOnClickListener {
                canvasPresenter.changeRectangleAttribute(HorizontalChange(-1))
            }

            //  y 좌표 변화
            posYUpDownView?.upButton?.setOnClickListener {
                canvasPresenter.changeRectangleAttribute(VerticalChange(1))
            }
            posYUpDownView?.downButton?.setOnClickListener {
                canvasPresenter.changeRectangleAttribute(VerticalChange(-1))
            }

            //  너비 변화
            sizeWidthUpDownView?.upButton?.setOnClickListener {
                canvasPresenter.changeRectangleAttribute(WidthChange(1))
            }
            sizeWidthUpDownView?.downButton?.setOnClickListener {
                canvasPresenter.changeRectangleAttribute(WidthChange(-1))
            }

            //  높이 변화
            sizeHeightUpDownView?.upButton?.setOnClickListener {
                canvasPresenter.changeRectangleAttribute(HeightChange(1))
            }
            sizeHeightUpDownView?.downButton?.setOnClickListener {
                canvasPresenter.changeRectangleAttribute(HeightChange(-1))
            }
        }
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

