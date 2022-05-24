package com.example.chat_2022_eleves

//import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class ConversationActivity : AppCompatActivity(), View.OnClickListener {

    var gs: GlobalState? = null
    var btnEnvoiMessage: Button? = null
    var champTxtMessage : EditText? = null
    var hash: String? = null
    var pseudo: String? = ""
    var conv: Conversation? = null
    var messages: ListMessages? = null
    var list = ArrayList<String>()
    lateinit var conversationRecyclerView: RecyclerView
    var skipBottomEvent = true
    var isAtBottom = true
//    var testPseudo = "toto"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_show_conversation)
        conversationRecyclerView = findViewById(R.id.conversationRecyclerView)
        conversationRecyclerView.layoutManager = LinearLayoutManager(this)

        champTxtMessage = findViewById(R.id.conversation_edtMessage)
        btnEnvoiMessage = findViewById(R.id.conversation_btnOK)

        btnEnvoiMessage?.setOnClickListener(this)

        conversationRecyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
//            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
//                super.onScrollStateChanged(recyclerView, newState)
//                if (!recyclerView.canScrollVertically(1)) {
//                    Toast.makeText(this@ConversationActivity, "Last", Toast.LENGTH_LONG).show()
//                    if(!skipBottomEvent && !isAtBottom) {
//                        getConvMessagesRequest()
//                    }
//                    else {
//                        skipBottomEvent = false
//                    }
//                    isAtBottom = false
//                }
//            }
        })

        gs = application as GlobalState
        val bdl = this.intent.extras
        val convString = bdl?.getString("data") ?: ""
        hash = bdl?.getString("hash")
        pseudo = bdl?.getString("pseudo") ?: ""
        conv = Gson().fromJson(convString, Conversation::class.java)

        getConvMessagesRequest()

        gs!!.alerter("data : " + (bdl?.getString("data") ?: ""))
        println(bdl)
    }

    override fun onResume() {
        super.onResume()
//        conversationRecyclerView.adapter?.notifyDataSetChanged()
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
                Log.i("get messages", res.toString())
                if (res?.success.toBoolean()) {
                    messages = res
                    print(messages?.getMessages())
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