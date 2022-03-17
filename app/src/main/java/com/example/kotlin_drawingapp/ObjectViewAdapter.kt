package com.example.kotlin_drawingapp

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView

class ObjectViewAdapter(
    private val context: Context,
    private var dataList: MutableList<ObjectData>
) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    //var dataList = mutableListOf<ObjectData>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view: View?
        return when (viewType) {
            RECTANGLE_OBJECT_TYPE -> {
                view = LayoutInflater.from(parent.context).inflate(
                    R.layout.rectangle_object_item,
                    parent,
                    false
                )
                RectangleViewHolder(view)
            }
            PICTURE_OBJECT_TYPE -> {
                view = LayoutInflater.from(parent.context).inflate(
                    R.layout.photo_object_item,
                    parent,
                    false
                )
                PictureViewHolder(view)
            }
            else -> {
                view = LayoutInflater.from(parent.context).inflate(
                    R.layout.text_object_item,
                    parent,
                    false
                )
                TextViewHolder(view)
            }
        }
    }

    inner class RectangleViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val container: ConstraintLayout = view.findViewById(R.id.rectangle_object_container)
        private val numberTextView: TextView = view.findViewById(R.id.rectangle_num_text_view)
        fun bind(item: ObjectData) {
            numberTextView.text = item.dataNumber.toString()
        }
    }

    inner class PictureViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val container: ConstraintLayout = view.findViewById(R.id.photo_object_container)
        private val numberTextView: TextView = view.findViewById(R.id.photo_num_text_view)
        fun bind(item: ObjectData) {
            numberTextView.text = item.dataNumber.toString()
        }
    }

    inner class TextViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val container: ConstraintLayout = view.findViewById(R.id.text_object_container)
        private val numberTextView: TextView = view.findViewById(R.id.text_num_text_view)
        fun bind(item: ObjectData) {
            numberTextView.text = item.dataNumber.toString()
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (dataList[position].type) {
            RECTANGLE_OBJECT_TYPE -> {
                (holder as RectangleViewHolder).bind(dataList[position])
                holder.setIsRecyclable(false)
            }
            PICTURE_OBJECT_TYPE -> {
                (holder as PictureViewHolder).bind(dataList[position])
                holder.setIsRecyclable(false)
            }
            else -> {
                (holder as TextViewHolder).bind(dataList[position])
                holder.setIsRecyclable(false)
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return dataList[position].type
    }

    override fun getItemCount(): Int = dataList.size

    fun updateReceiptsList(newList: MutableList<ObjectData>) {
        dataList = newList
        notifyDataSetChanged()
    }

}