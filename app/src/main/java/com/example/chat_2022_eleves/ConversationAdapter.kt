package com.example.chat_2022_eleves

import android.annotation.SuppressLint
import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.ColorInt
import androidx.recyclerview.widget.RecyclerView
import com.example.chat_2022_eleves.databinding.MessageBinding


/**
 * Created by Martin Grabarz on 22/05/2022.
 */
class ConversationAdapter(val items: ArrayList<Message?>?) : RecyclerView.Adapter<ConversationAdapter.ViewHolder>() {
    val BLACK = "#FF000000"
    val WHITE = "FFFFFFFF"
    val RED = "F44336"
    val GREEN = "009688"
    val BLUE = "2196F3"

    inner class ViewHolder(val binding: MessageBinding) : RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("ResourceAsColor")
        fun bindAndVersion(message: Message) {
            with(message) {
                binding.messageTxt.text = message.contenu
//                var a = findColor(message.couleur!!)
//                var b = R.color.black.
                var c = Color.parseColor(R.color.black.toString())
                binding.bubble.setBackgroundColor(Color.parseColor(R.color.black.toString()))
//                binding.bubble.setBackgroundColor(findColor(message.couleur!!))
            }
        }
    }

    override fun getItemCount(): Int = items!!.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = MessageBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindAndVersion(items!![position]!!)
    }

    private fun findColor(color : String): Int {
        return when (color) {
            "black" -> Color.parseColor(R.color.black.toString())
            "white" -> Color.parseColor(R.color.white.toString())
            "red" -> Color.parseColor(R.color.red.toString())
            "green" -> Color.parseColor(R.color.green.toString())
            "blue" -> Color.parseColor(R.color.blue.toString())
            // 'else' is not required because all cases are covered
            else -> Color.parseColor(R.color.white.toString())
        }
    }

}

