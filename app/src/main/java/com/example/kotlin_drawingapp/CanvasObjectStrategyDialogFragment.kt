package com.example.kotlin_drawingapp

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.DialogFragment

class CanvasObjectStrategyDialogFragment(val listener: DialogClickListener) :
    DialogFragment() {
    private lateinit var mostBackButton: Button
    private lateinit var mostForthButton: Button
    private lateinit var forthButton: Button
    private lateinit var backButton: Button

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val myView =
            inflater.inflate(R.layout.fragment_canvas_object_strategy_dialog, container, false)

        mostBackButton = myView.findViewById<Button>(R.id.set_most_back_button)
        mostForthButton = myView.findViewById<Button>(R.id.set_most_forth_button)
        forthButton = myView.findViewById<Button>(R.id.set_forth_button)
        backButton = myView.findViewById<Button>(R.id.set_back_button)

        setButtonsListener()


        return myView
    }

    private fun setButtonsListener() {
        mostBackButton.setOnClickListener {
            listener.onMostBackClicked()
            dismiss()
        }
        backButton.setOnClickListener {
            listener.onBackClicked()
            dismiss()
        }
        mostForthButton.setOnClickListener {
            listener.onMostForthClicked()
            dismiss()
        }
        forthButton.setOnClickListener {
            listener.onForthClicked()
            dismiss()
        }
    }

}