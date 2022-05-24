package com.example.chat_2022_eleves.model

class Conversation(id: String, active: String, theme: String) {
    val id: String? = null
    var active: String? = null
    private var theme: String? = null

    fun getActive(): Boolean {
        return active == "1"
    }

    fun getTheme(): String? {
        return theme
    }

    override fun toString(): String {
        return "Conversation(id=$id, active=$active, theme=$theme)"
    }
}