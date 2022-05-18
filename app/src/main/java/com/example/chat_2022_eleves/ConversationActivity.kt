package com.example.chat_2022_eleves

import android.os.Bundle
import android.support.v7.app.AppCompatActivity

class ConversationActivity : AppCompatActivity() {

    var gs: GlobalState? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        gs = application as GlobalState
        val bdl = this.intent.extras
        gs!!.alerter("data : " + (bdl?.getString("data") ?: ""))
    }

}