package com.example.chat_2022_eleves

class Conversation {
    var id: String? = null
    var active: String? = null
    private var theme: String? = null

    // {"id":"23","active":"0","theme":"test"}
    override fun toString(): String {
        return "Conversation{" +
                "id='" + id + '\'' +
                ", active='" + active + '\'' +
                ", theme='" + theme + '\'' +
                '}'
    }

    fun getActive(): Boolean {
        return active == "1"
    }

    fun getTheme(): String? {
        return theme
    }
}