package com.example.chat_2022_eleves.model

import com.example.chat_2022_eleves.model.Conversation
import java.util.ArrayList

class ListConversations {
    var version: String? = null
    var success: String? = null
    var status: String? = null
    private var conversations: ArrayList<Conversation?>? = null

    override fun toString(): String {
        return "ListConversations{" +
                "version='" + version + '\'' +
                ", success='" + success + '\'' +
                ", status='" + status + '\'' +
                ", conversations=" + conversations +
                '}'
    }

    fun getConversations(): ArrayList<Conversation?>? {
        return conversations
    }
}