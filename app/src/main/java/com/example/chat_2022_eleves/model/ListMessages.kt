package com.example.chat_2022_eleves.model

import com.example.chat_2022_eleves.model.Message
import java.util.ArrayList

class ListMessages {

    var version: String? = null
    var success: String? = null
    var status: String? = null
    private var messages: ArrayList<Message?>? = null

    fun getMessages(): ArrayList<Message?>? {
        return messages
    }

    override fun toString(): String {
        return "ListMessages{\n" +
                "\tversion=$version, \n" +
                "\tsuccess=$success, \n" +
                "\tstatus=$status, \n" +
                "\tmessages=$messages)"
    }
}