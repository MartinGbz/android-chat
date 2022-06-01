package com.example.chat_2022_eleves

import android.annotation.SuppressLint
import android.content.SharedPreferences
import android.graphics.Color
import android.graphics.Color.BLACK
import android.graphics.drawable.Drawable
import android.graphics.drawable.ShapeDrawable
import android.view.Gravity
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.recyclerview.widget.RecyclerView
import com.example.chat_2022_eleves.databinding.MessageBinding
import com.example.chat_2022_eleves.model.Message


/**
 * Created by Martin Grabarz on 22/05/2022.
 */
class ConversationAdapter(val items: ArrayList<Message?>?, val pseudo: String) : RecyclerView.Adapter<ConversationAdapter.ViewHolder>() {

    var shapeDrawable : Drawable? = null

    inner class ViewHolder(val binding: MessageBinding) : RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("ResourceAsColor", "RtlHardcoded")
        fun bindAndVersion(message: Message, pseudo: String) {
            with(message) {
                binding.messageTxt.text = message.contenu
                binding.auteurTxt.text = message.auteur
                println(pseudo)
                val params : FrameLayout.LayoutParams
                if(message.auteur == pseudo) {
                    params = FrameLayout.LayoutParams(
                        FrameLayout.LayoutParams.WRAP_CONTENT,
                        FrameLayout.LayoutParams.WRAP_CONTENT
                    ).apply {
                        bottomMargin = 30
                        gravity = Gravity.RIGHT
                    }
                }
                else {
                    params = FrameLayout.LayoutParams(
                        FrameLayout.LayoutParams.WRAP_CONTENT,
                        FrameLayout.LayoutParams.WRAP_CONTENT
                    ).apply {
                        bottomMargin = 30
                        gravity = Gravity.LEFT
                    }
                }
                binding.bubble.layoutParams = params
                shapeDrawable = binding.bubble.background
                setBgColor(message.couleur!!, shapeDrawable!!)
            }
        }
    }

    override fun getItemCount(): Int = items!!.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = MessageBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindAndVersion(items!![position]!!, pseudo)
    }

    private fun setBgColor(color : String, shapeDrawable : Drawable) {
        when (color) {
            "black" -> shapeDrawable.setTint(Color.GRAY)
            "white" -> shapeDrawable.setTint(Color.WHITE)
            "red" -> shapeDrawable.setTint(Color.RED)
            "green" -> shapeDrawable.setTint(Color.GREEN)
            "blue" -> shapeDrawable.setTint(Color.BLUE)
            else -> shapeDrawable.setTint(Color.WHITE)
        }
    }

}

