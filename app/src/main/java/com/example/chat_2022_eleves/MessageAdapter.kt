package com.example.chat_2022_eleves

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter


/**
 * Created by Martin Grabarz on 22/05/2022.
 */
class MessageAdapter : BaseAdapter() {
    var messages: MutableList<Message> = ArrayList()
    var context: Context? = null

    fun MessageAdapter(context: Context?) {
        this.context = context
    }

    fun add(message: Message) {
        messages.add(message)
        notifyDataSetChanged() // to render the list we need to notify
    }

    override fun getCount(): Int {
        return messages.size
    }

    override fun getItem(i: Int): Any? {
        return messages[i]
    }

    override fun getItemId(i: Int): Long {
        return i.toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        TODO("Not yet implemented")
    }

    // This is the backbone of the class, it handles the creation of single ListView row (chat bubble)
//    override fun getView(i: Int, convertView: View, viewGroup: ViewGroup?): View? {
//        var convertView: View = convertView
//        val holder = MessageViewHolder()
//        val messageInflater =
//            context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE) as LayoutInflater
//        val message = messages[i]
//        if (message.isBelongsToCurrentUser()) { // this message was sent by us so let's create a basic chat bubble on the right
//            convertView = messageInflater.inflate(R.layout.my_message, null)
//            holder.messageBody = convertView.findViewById(R.id.message_body)
//            convertView.setTag(holder)
//            holder.messageBody.setText(message.getText())
//        } else { // this message was sent by someone else so let's create an advanced chat bubble on the left
//            convertView = messageInflater.inflate(R.layout.their_message, null)
//            holder.avatar = convertView.findViewById(R.id.avatar) as View
//            holder.name = convertView.findViewById(R.id.name)
//            holder.messageBody = convertView.findViewById(R.id.message_body)
//            convertView.setTag(holder)
//            holder.name.setText(message.getMemberData().getName())
//            holder.messageBody.setText(message.getText())
//            val drawable = holder.avatar.getBackground() as GradientDrawable
//            drawable.setColor(Color.parseColor(message.getMemberData().getColor()))
//        }
//        return convertView
//    }
}