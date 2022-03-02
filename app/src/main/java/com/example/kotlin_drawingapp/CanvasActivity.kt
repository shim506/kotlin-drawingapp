package com.example.kotlin_drawingapp

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.util.TypedValue
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout

import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.core.view.marginTop
import androidx.window.layout.WindowMetricsCalculator
import com.example.kotlin_drawingapp.CanvasContract.Present
import com.example.kotlin_drawingapp.data.Rectangle
import com.example.kotlin_drawingapp.data.RectangleFactory
import com.example.kotlin_drawingapp.databinding.ActivityMainBinding
import com.orhanobut.logger.AndroidLogAdapter
import com.orhanobut.logger.Logger

private lateinit var binding: ActivityMainBinding

lateinit var canvasPresent: Present

class MainActivity : AppCompatActivity(), CanvasContract.View {
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
            canvasPresent.addRectangle()

           /* val rect = RectangleFactory().createRectangle(widthDp, heightDp)
            Logger.d(rect.toString())
            val view = View(this)
            view.id = View.generateViewId()
            val lp = ConstraintLayout.LayoutParams(rect.size.width, rect.size.height)
            view.setBackgroundColor(
                Color.argb(rect.getAlpha(), rect.rgba.r, rect.rgba.g, rect.rgba.b)
            )
            view.layoutParams = lp
            binding.container.addView(view)

            setConstraint(view, rect.point.x, rect.point.y)*/
        }
    }

    override fun showRectangle(rectangleList:MutableList<Rectangle>) {


    }

    private fun setConstraint(view: View, marginLeftDp: Int, marginTopDp: Int) {
        val set = ConstraintSet()
        set.clone(binding.container)
        set.connect(
            view.id,
            ConstraintSet.TOP,
            binding.container.id,
            ConstraintSet.TOP,
            convertDpToPx(marginTopDp)
        )
        set.connect(
            view.id,
            ConstraintSet.LEFT,
            binding.container.id,
            ConstraintSet.LEFT,
            convertDpToPx(marginLeftDp)
        )
        set.applyTo(binding.container)
    }

    private fun convertDpToPx(value: Int): Int {
        return TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            value.toFloat(),
            resources.displayMetrics
        ).toInt()
    }

    private fun loggerInitialize() {
        Logger.addLogAdapter(AndroidLogAdapter())
    }


}