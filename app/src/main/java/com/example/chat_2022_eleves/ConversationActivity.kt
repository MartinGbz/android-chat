package com.example.chat_2022_eleves

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.Button
import android.widget.EditText
import com.google.gson.Gson
import org.json.JSONObject

//import org.json.JSONObject

import kotlinx.serialization.*
import kotlinx.serialization.json.*

class ConversationActivity : AppCompatActivity(), View.OnClickListener {

    var gs: GlobalState? = null
    var btnEnvoiMessage: Button? = null
    var champTxtMessage : EditText? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_show_conversation)
        champTxtMessage = findViewById(R.id.conversation_edtMessage)
        btnEnvoiMessage = findViewById(R.id.conversation_btnOK)
        btnEnvoiMessage?.setOnClickListener(this)
        gs = application as GlobalState
        val bdl = this.intent.extras
//        val convData = gson.fromJson("\"\"\""+(bdl?.getString("data"))+"\"\"\"")
        val convDataString = (bdl?.getString("data"))
        val eheh = Gson().fromJson(bdl?.getString("data"), Conversation::class.java)
        val id = eheh.id
//        val a = Conversation(bdl?.getString("data").id)
        val test = "\"\"\""+convDataString+"\"\"\""
        val q = Json.parseToJsonElement((bdl?.getString("data") ?: ""))
//        val oo = JSONObject((bdl?.getString("data") ?: ""))
//        val convData = Gson().fromJson("\"\"\""+convDataString+"\"\"\"", Conversation::class.java);

//        val convData = JSON.parse(bdl?.getString("data"));
//        val convData = JSONObject(bdl?.getString("data"));
//        val convData = bdl?.getString("data")?.let { JSONObject(it) }
        gs!!.alerter("data : " + (bdl?.getString("data") ?: ""))
        println(bdl);
//        println(convData);
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