package com.example.kotlin_drawingapp.customView

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import com.example.kotlin_drawingapp.R

class UpDownView(context: Context, attrs: AttributeSet) : ConstraintLayout(context, attrs) {
    lateinit var upButton: ImageButton
    lateinit var downButton: ImageButton
    lateinit var attr: TextView
    lateinit var value: TextView

    init {
        val infService = Context.LAYOUT_INFLATER_SERVICE;
        val li = getContext().getSystemService(infService) as LayoutInflater;
        li.inflate(R.layout.up_down_view, this, true);

        upButton = findViewById(R.id.up_image_button)
        downButton = findViewById(R.id.down_image_button)
        attr = findViewById(R.id.attr)
        value = findViewById(R.id.value)

    }


}