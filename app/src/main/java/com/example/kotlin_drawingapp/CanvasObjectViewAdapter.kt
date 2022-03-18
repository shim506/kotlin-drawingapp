package com.example.kotlin_drawingapp

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.kotlin_drawingapp.data.CanvasObjectType
import com.example.kotlin_drawingapp.data.model.Plane
import com.example.kotlin_drawingapp.orderstratgy.BackStrategy
import com.example.kotlin_drawingapp.orderstratgy.ForthStrategy
import com.example.kotlin_drawingapp.orderstratgy.MostBackStrategy
import com.example.kotlin_drawingapp.orderstratgy.MostForthStrategy

class CanvasObjectViewAdapter(
    private val context: Context,
    private var dataListCanvas: MutableList<CanvasObjectData>,
    private val presenter: CanvasContract.Presenter
) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view: View?
        return when (viewType) {
            CanvasObjectType.RECTANGLE.value -> {
                view = LayoutInflater.from(parent.context).inflate(
                    R.layout.rectangle_object_item,
                    parent,
                    false
                )
                RectangleViewHolder(view)
            }
            CanvasObjectType.PICTURE.value -> {
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
        fun bind(item: CanvasObjectData) {
            numberTextView.text = item.dataNumber.toString()
        }
    }

    inner class PictureViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val container: ConstraintLayout = view.findViewById(R.id.photo_object_container)
        private val numberTextView: TextView = view.findViewById(R.id.photo_num_text_view)
        fun bind(item: CanvasObjectData) {
            numberTextView.text = item.dataNumber.toString()
        }
    }

    inner class TextViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val container: ConstraintLayout = view.findViewById(R.id.text_object_container)
        private val numberTextView: TextView = view.findViewById(R.id.text_num_text_view)
        fun bind(item: CanvasObjectData) {
            numberTextView.text = item.dataNumber.toString()
        }

    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        when (dataListCanvas[position].type) {
            CanvasObjectType.RECTANGLE -> {
                (holder as RectangleViewHolder).bind(dataListCanvas[position])
                holder.setIsRecyclable(false)
            }
            CanvasObjectType.PICTURE -> {
                (holder as PictureViewHolder).bind(dataListCanvas[position])
                holder.setIsRecyclable(false)
            }
            else -> {
                (holder as TextViewHolder).bind(dataListCanvas[position])
                holder.setIsRecyclable(false)
            }
        }

        holder.itemView.setOnLongClickListener {

            with(dataListCanvas[position]) {
                val dialog = CanvasObjectStrategyDialogFragment(object : DialogClickListener {
                    override fun onBackClicked() {

                        // dataList 도 업데이트
                        // 실제 그려지는 것도 업데이트
                        presenter.changeCanvasObjectOrder(
                            BackStrategy(this@with.type, this@with.rectangle)
                        )
                    }

                    override fun onMostBackClicked() {
                        presenter.changeCanvasObjectOrder(
                            MostBackStrategy(this@with.type, this@with.rectangle)
                        )
                    }

                    override fun onForthClicked() {
                        presenter.changeCanvasObjectOrder(
                            ForthStrategy(this@with.type, this@with.rectangle)
                        )
                    }

                    override fun onMostForthClicked() {
                        presenter.changeCanvasObjectOrder(
                            MostForthStrategy(this@with.type, this@with.rectangle)
                        )
                    }
                }

                )
                dialog.show((context as FragmentActivity).supportFragmentManager, "얄루얄루")
                return@setOnLongClickListener (true)
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return dataListCanvas[position].type.value
    }

    override fun getItemCount(): Int = dataListCanvas.size

    fun updateReceiptsList(newList: MutableList<CanvasObjectData>) {
        dataListCanvas = newList
        notifyDataSetChanged()
    }

}

interface DialogClickListener {
    fun onBackClicked()
    fun onMostBackClicked()
    fun onForthClicked()
    fun onMostForthClicked()
}
