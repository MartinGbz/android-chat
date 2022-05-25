package com.example.chat_2022_eleves

//import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.chat_2022_eleves.api.APIClient
import com.example.chat_2022_eleves.api.APIInterface
import com.example.chat_2022_eleves.api.PostMessageResponse
import com.example.chat_2022_eleves.model.Conversation
import com.example.chat_2022_eleves.model.ListMessages
import com.google.gson.Gson
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class ConversationActivity : AppCompatActivity(), View.OnClickListener {

    var gs: GlobalState? = null
    var btnEnvoiMessage: Button? = null
    var convTitle: TextView? = null
    var champTxtMessage : EditText? = null
    var hash: String? = null
    var pseudo: String? = ""
    var conv: Conversation? = null
    var messages: ListMessages? = null
    lateinit var conversationRecyclerView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_show_conversation)
        conversationRecyclerView = findViewById(R.id.conversationRecyclerView)
        conversationRecyclerView.layoutManager = LinearLayoutManager(this)

        champTxtMessage = findViewById(R.id.conversation_edtMessage)
        btnEnvoiMessage = findViewById(R.id.conversation_btnOK)

        btnEnvoiMessage?.setOnClickListener(this)

        gs = application as GlobalState

        val bdl = this.intent.extras
        val convString = bdl?.getString("data") ?: ""
        conv = Gson().fromJson(convString, Conversation::class.java)
        hash = bdl?.getString("hash")
        pseudo = bdl?.getString("pseudo") ?: ""

        convTitle = findViewById(R.id.convTitle)
        convTitle?.text = conv?.getTheme()

        getConvMessagesRequest()

        gs!!.alerter("data : " + (bdl?.getString("data") ?: ""))
    }

    override fun onClick(view: View?) {
        view?.let {
            when (view.id) {
                R.id.conversation_btnOK -> {
                    gs!!.alerter("message : " + champTxtMessage?.text.toString())
                    postMessageRequest(champTxtMessage?.text.toString())
                }
                else -> println("Unknown")
            }
        }
    }

    private fun getConvMessagesRequest(): Unit? {
        val apiService = APIClient.getClient()?.create(APIInterface::class.java)
        val call1 = apiService?.doGetConvMessages(this.hash, this.conv?.id.toString())
        return call1?.enqueue(object : Callback<ListMessages?> {
            override fun onResponse(
                call: Call<ListMessages?>?,
                response: Response<ListMessages?>?
            ) {
                val res = response?.body()
                Log.i("GET MESSAGE", res.toString())
                if (res?.success.toBoolean()) {
                    messages = res
                    conversationRecyclerView.adapter = ConversationAdapter(messages?.getMessages(), pseudo!!)
                    conversationRecyclerView.adapter?.notifyDataSetChanged()
                    conversationRecyclerView.scrollToPosition(messages?.getMessages()!!.size-1)
                }
            }

            override fun onFailure(call: Call<ListMessages?>?, t: Throwable?) {
                call?.cancel()
            }
        })
    }

    private fun postMessageRequest(message: String): Unit? {
        val apiService = APIClient.getClient()?.create(APIInterface::class.java)
        val call1 = apiService?.doPostMessage(this.hash, this.conv?.id.toString(), message)
        return call1?.enqueue(object : Callback<PostMessageResponse?> {
            override fun onResponse(
                call: Call<PostMessageResponse?>?,
                response: Response<PostMessageResponse?>?
            ) {
                val res = response?.body()
                champTxtMessage?.text?.clear()

                getConvMessagesRequest()
                Log.i("POST MESSAGE", res.toString())
            }
            override fun onFailure(call: Call<PostMessageResponse?>?, t: Throwable?) {
                call?.cancel()
            }
        })
    }

}