package com.example.chat_2022_eleves

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.chat_2022_eleves.databinding.ActivityShowConversationBinding
import com.example.chat_2022_eleves.databinding.MessageBinding
import java.util.ArrayList

/**
 * Created by Martin Grabarz on 22/05/2022.
 */
class ConversationAdapter(val items: ArrayList<Message?>?) : RecyclerView.Adapter<ConversationAdapter.ViewHolder>() {

    inner class ViewHolder(val binding: MessageBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bindAndVersion(message: Message) {
            with(message) {
                binding.messageTxt.text = message.contenu
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

}

