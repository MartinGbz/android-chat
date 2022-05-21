package com.example.chat_2022_eleves

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.google.gson.Gson

class ConversationActivity : AppCompatActivity(), View.OnClickListener {

    var gs: GlobalState? = null
    var btnEnvoiMessage: Button? = null
    var champTxtMessage : EditText? = null
    var svMessages: ScrollView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_show_conversation)
        champTxtMessage = findViewById(R.id.conversation_edtMessage)
        btnEnvoiMessage = findViewById(R.id.conversation_btnOK)
        btnEnvoiMessage?.setOnClickListener(this)
        gs = application as GlobalState
        val bdl = this.intent.extras
        val convString = bdl?.getString("data") ?: ""
        val conv = Gson().fromJson(convString, Conversation::class.java)
        val id = conv.id
        gs!!.alerter("data : " + (bdl?.getString("data") ?: ""))
        println(bdl)

        svMessages = findViewById<View?>(R.id.conversation_svMessages) as ScrollView

        // svMessages.
        // var obj = [{"id":"625"";contenu";"test";"auteur":"castor","couleur":"black"}]


    }

    override fun onClick(view: View?) {
        view?.let {
            when (view.id) {
                R.id.conversation_btnOK -> {
                    gs!!.alerter("message : " + champTxtMessage?.text.toString())
                }
                else -> println("Unknown")
            }
        }
    }

}