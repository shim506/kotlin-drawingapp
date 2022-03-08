package com.example.kotlin_drawingapp


import android.content.ContentResolver
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.PackageManagerCompat
import androidx.core.graphics.decodeBitmap
import com.example.kotlin_drawingapp.CanvasContract.Presenter
import com.example.kotlin_drawingapp.data.Picture
import com.example.kotlin_drawingapp.data.Rectangle
import com.example.kotlin_drawingapp.data.repository.LocalTextFileRepository
import com.example.kotlin_drawingapp.databinding.ActivityMainBinding
import com.orhanobut.logger.AndroidLogAdapter
import com.orhanobut.logger.Logger
import java.io.BufferedInputStream
import java.io.FileInputStream
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
                    val uri = it.data?.data as Uri
                    val bitmap = getBitmapWithUri(uri)

                    myCanvas = myCanvasInitialize()
                    binding.canvasContainer.addView(myCanvas)
                    canvasPresenter.addImageRectangle(bitmap)
                }
            }
        binding.imageAddButton?.setOnClickListener {
            intent = Intent(Intent.ACTION_GET_CONTENT)
            intent.type = "image/*"
            getResult.launch(intent)
        }
    }

    private fun getBitmapWithUri(uri: Uri): Bitmap {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            val source = ImageDecoder.createSource(this.contentResolver, uri)
            ImageDecoder.decodeBitmap(source)
        } else {
            MediaStore.Images.Media.getBitmap(contentResolver, uri)
        }
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