package com.example.chat_2022_eleves

import android.annotation.SuppressLint
import android.content.SharedPreferences
import android.view.Gravity
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.LinearLayout
import androidx.annotation.ColorInt
import androidx.recyclerview.widget.RecyclerView
import com.example.chat_2022_eleves.databinding.MessageBinding


/**
 * Created by Martin Grabarz on 22/05/2022.
 */
class ConversationAdapter(val items: ArrayList<Message?>?, val testPseudo: String) : RecyclerView.Adapter<ConversationAdapter.ViewHolder>() {
//    val BLACK = "#FF000000"
//    val WHITE = "#FFFFFFFF"
//    val RED = "#F44336"
//    val GREEN = "#009688"
//    val BLUE = "#2196F3"

    @ColorInt
    val BLACK = 0xff000000

    @ColorInt
    val DKGRAY = -0xbbbbbc

    @ColorInt
    val GRAY = -0x777778

    @ColorInt
    val LTGRAY = -0x333334

    @ColorInt
    val WHITE = -0x1

    @ColorInt
    val RED = -0x10000

    @ColorInt
    val GREEN = -0xff0100

    @ColorInt
    val BLUE = -0xffff01

    @ColorInt
    val YELLOW = -0x100

    @ColorInt
    val CYAN = -0xff0001

    @ColorInt
    val MAGENTA = -0xff01

    @ColorInt
    val TRANSPARENT = 0

    var sp: SharedPreferences? = null

    inner class ViewHolder(val binding: MessageBinding) : RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("ResourceAsColor", "RtlHardcoded")
        fun bindAndVersion(message: Message, testPseudo: String) {
            with(message) {
                binding.messageTxt.text = message.contenu
                binding.auteurTxt.text = message.auteur
                println(testPseudo)
//                binding.bubble.setHorizontalGravity(Gravity.RIGHT)
                val params : FrameLayout.LayoutParams
                if(message.auteur == testPseudo) {
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
//                binding.bubble.gravity = Gravity.RIGHT
//                binding.bubble.setHorizontalGravity(Gravity.RIGHT)
//                binding.bubble.setVerticalGravity(Gravity.RIGHT)

//                var a = Color.parseColor("#FF0000")
//                var a = 0xFF000000
//                var a = findColor(message.couleur!!)
//                binding.bubble.setBackgroundColor(BLACK.toInt())
            }
        }
    }

    override fun getItemCount(): Int = items!!.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = MessageBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindAndVersion(items!![position]!!, testPseudo)
    }

//    private fun findColor(color : String): Int {
//        return when (color) {
//            "black" -> Color.parseColor(BLACK)
//            "white" -> Color.parseColor(WHITE)
//            "red" -> Color.parseColor(RED)
//            "green" -> Color.parseColor(GREEN)
//            "blue" -> Color.parseColor(BLUE)
//            // 'else' is not required because all cases are covered
//            else -> Color.parseColor(WHITE)
//        }
//    }

}

