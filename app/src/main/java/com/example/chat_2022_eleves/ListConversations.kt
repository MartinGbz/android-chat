package com.example.chat_2022_eleves

import java.util.ArrayList

class ListConversations {
    var version: String? = null
    var success: String? = null
    var status: String? = null
    private var conversations: ArrayList<Conversation?>? = null

    //{"version":1.3,"success":true,"status":200,"conversations"
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