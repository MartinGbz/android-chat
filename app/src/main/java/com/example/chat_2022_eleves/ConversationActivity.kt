package com.example.chat_2022_eleves

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ScrollView
import android.widget.Spinner
import com.google.gson.Gson
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

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

    private fun getConvMessagesRequest(): Unit? {
        val apiService = APIClient.getClient()?.create(APIInterface::class.java)
        val call1 = apiService?.doPostAuthentication(
            "tomnab.fr",

        )
        return call1?.enqueue(object : Callback<AuthenticationResponse?> {
            override fun onResponse(
                call: Call<AuthenticationResponse?>?,
                response: Response<AuthenticationResponse?>?
            ) {
                val res = response?.body()
                Log.i(GlobalState.CAT, res.toString())
                if (res?.success.toBoolean()) {
                    if (res?.hash === "") return
                    val versChoixConv = Intent(this@LoginActivity, ChoixConvActivity::class.java)
                    val bdl = Bundle()
                    bdl.putString("hash", res?.hash)
                    versChoixConv.putExtras(bdl)
                    startActivity(versChoixConv)
                }
            }

            override fun onFailure(call: Call<AuthenticationResponse?>?, t: Throwable?) {
                call?.cancel()
            }
        })
    }

}