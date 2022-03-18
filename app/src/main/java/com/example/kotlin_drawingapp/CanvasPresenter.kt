package com.example.kotlin_drawingapp

import android.app.Activity
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.widget.Toast
import com.example.kotlin_drawingapp.changeAttr.IChangeAttribute
import com.example.kotlin_drawingapp.changeAttr.PositiveLimitException
import com.example.kotlin_drawingapp.data.*
import com.example.kotlin_drawingapp.data.model.Plane
import com.example.kotlin_drawingapp.data.repository.LocalTextRepository
import com.example.kotlin_drawingapp.data.repository.RectangleRepository
import com.example.kotlin_drawingapp.data.model.selected.ISelected
import com.example.kotlin_drawingapp.orderstratgy.CanvasObjectOrderStrategy
import java.io.ByteArrayOutputStream
import java.lang.StringBuilder

const val WORD_NUMBER = 5
const val WORD_HEIGHT = 50 // PX
const val WORD_WIDTH = 400 // PX

class CanvasPresenter(
    private val canvasView: CanvasContract.View,
    private val repository: RectangleRepository
) : CanvasContract.Presenter {
    val plane = Plane

    init {
        repository.loadRectangles("url")?.let { plane.rectangleList = it.toMutableList() }

    }

    override fun addRectangle() {
        val rect = createRectangle()
        plane.addRectangle(rect, object : PlaneRectangleAddListener {
            override fun onEvent(rectangleList: MutableList<Rectangle>) {
                canvasView.showAll(
                    plane.rectangleList,
                    plane.pictureList,
                    plane.selectedRecList,
                    plane.textList
                )
            }
        })
        canvasView.addCanvasObjectData(
            CanvasObjectData(
                CanvasObjectType.RECTANGLE,
                rect,
                rect.getNumber()
            )
        )
    }

    override fun addImageRectangle(bitmap: Bitmap) {
        val stream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream)
        val rect = createImageRectangle()
        val picture = Picture(stream.toByteArray(), rect)
        plane.addImageRectangle(
            picture,
            object : PlaneImageAddListener {
                override fun onEvent(pictureList: MutableList<Picture>) {
                    canvasView.showAll(
                        plane.rectangleList,
                        plane.pictureList,
                        plane.selectedRecList, plane.textList
                    )
                }
            })
        canvasView.addCanvasObjectData(
            CanvasObjectData(
                CanvasObjectType.PICTURE,
                rect,
                picture.getNumber()
            )
        )
    }

    override fun addText() {
        val rec = createTextRectangle()
        val text = LocalTextRepository.loadText()
        val randomText = subtractRandomText(text, WORD_NUMBER)
        val newText = Text(randomText, rec)
        plane.addText(newText)
        canvasView.showAll(
            plane.rectangleList,
            plane.pictureList,
            plane.selectedRecList, plane.textList
        )
        canvasView.addCanvasObjectData(
            CanvasObjectData(
                CanvasObjectType.TEXT,
                rec,
                newText.getNumber()
            )
        )
    }

    override fun changeCanvasObjectOrder(strategy: CanvasObjectOrderStrategy) {
        plane.changeOrder(strategy)
        canvasView.showAll(
            plane.rectangleList,
            plane.pictureList,
            plane.selectedRecList, plane.textList
        )
    }

    private fun subtractRandomText(text: String, wordNumber: Int): String {
        val textList = text.split(" ")

        val st = StringBuilder()
        repeat(wordNumber) {
            val idx = (textList.indices).random()
            st.append("${textList[idx]} ")
        }
        return st.toString()
    }

    override fun addImageRectangleWithUri(uri: Uri) {
        val bitmap = getBitmapWithUri(uri)
        addImageRectangle(bitmap)
    }

    override fun getSelected(): ISelected? {
        return plane.selected
    }

    override fun getSelectedRectangle(): Rectangle? {
        return plane.selected?.getRectangle()
    }

    override fun moveRectangle(rectangle: Rectangle?, x: Int, y: Int) {
        plane.moveRectangle(rectangle, x, y)
        canvasView.showAll(
            plane.rectangleList,
            plane.pictureList,
            plane.selectedRecList,
            plane.textList
        )
        canvasView.showSelectedAttribute(plane.selected?.getRectangle())
    }

    override fun changeRectangleAttribute(changeAttribute: IChangeAttribute) {
        try {
            plane.getSelectedRectangle()?.let { changeAttribute.applyChange(it) }
            canvasView.showAll(
                plane.rectangleList,
                plane.pictureList,
                plane.selectedRecList,
                plane.textList
            )
            canvasView.showSelectedAttribute(plane.getSelectedRectangle())
        } catch (e: PositiveLimitException) {
            Toast.makeText(canvasView as MainActivity, "1 미만의 값을 가질 수 없습니다", Toast.LENGTH_SHORT)
                .show()
        }
    }

    override fun setSelectedRectangle(x: Int, y: Int) {
        plane.setSelectedRectangle(x, y)
        val colorText: String = getSelectedColor(plane.getSelectedRectangle())
        canvasView.showSelectedColor(colorText)
        canvasView.showSelectedAlpha(plane.getSelectedRectangle())
        canvasView.showSelectedBound(plane.selectedRecList)
        canvasView.showSelectedAttribute(plane.getSelectedRectangle())
    }

    private fun getSelectedColor(selectedRec: Rectangle?): String {
        val colorText =
            selectedRec?.let {
                with(selectedRec.rgba) {
                    if (this.r != -1) {
                        java.lang.String.format("#%02X%02X%02X", this.r, this.g, this.b)
                    } else "null"
                }
            } ?: "null"
        return colorText
    }

    override fun changeRectangleColor() {
        plane.selected?.let {
            it.changeToRandomColor()
            canvasView.showRectangle(plane.rectangleList)
            canvasView.showSelectedColor(getSelectedColor(it.getRectangle()))
        } ?: kotlin.run { return }
    }

    override fun changeRectangleAlpha(value: Float) {
        plane.selected?.getRectangle()?.let {
            val alphaList = AlphaEnum.values()
            it.rgba.a = alphaList[(value.toInt() - 1)]
            canvasView.showRectangle(plane.rectangleList)
            canvasView.showSelectedColor(getSelectedColor(it))
        } ?: kotlin.run { return }
    }

    private fun createRectangle(): Rectangle {
        val (widthDP, heightDP) = canvasView.getWindowSize()
        return Rectangle.createRectangle(widthDP.toFloat(), heightDP.toFloat())
    }

    private fun createRectangleForOther(): Rectangle {
        val (widthDP, heightDP) = canvasView.getWindowSize()
        return Rectangle.createRectangleForOther(widthDP.toFloat(), heightDP.toFloat())
    }

    private fun createImageRectangle(): Rectangle {
        val rec = createRectangleForOther()
        rec.rgba.r = -1
        rec.rgba.g = -1
        rec.rgba.b = -1
        return rec
    }

    private fun createTextRectangle(): Rectangle {
        val rec = createRectangleForOther()
        rec.size.height = WORD_HEIGHT
        rec.size.width = WORD_WIDTH
        rec.rgba.r = -1
        rec.rgba.g = -1
        rec.rgba.b = -1
        return rec
    }

    private fun getBitmapWithUri(uri: Uri): Bitmap {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            val source = ImageDecoder.createSource((canvasView as Activity).contentResolver, uri)
            ImageDecoder.decodeBitmap(source)
        } else {
            MediaStore.Images.Media.getBitmap((canvasView as Activity).contentResolver, uri)
        }
    }
}

interface PlaneRectangleAddListener {
    fun onEvent(rectangleList: MutableList<Rectangle>)
}

interface PlaneImageAddListener {
    fun onEvent(pictureList: MutableList<Picture>)
}