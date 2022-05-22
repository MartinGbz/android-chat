package com.example.chat_2022_eleves

import android.os.Bundle
//import android.support.v7.app.AppCompatActivity
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
//    var svMessages: ScrollView? = null
    var lvMessages: ListView? = null
    var hash: String? = null
    var conv: Conversation? = null
    var messages: ListMessages? = null
    var list = ArrayList<String>()
    var arrayAdapter: Adapter? = null
    lateinit var conversationRecyclerView: RecyclerView
//    val arrayAdapter: Adapter? = null
//    var arrayAdapter2: MessageAdapter? = null
//    var list: Array<String?> = arrayOfNulls(0)
//    var list: ArrayList<String?>? = arrayListOf(null)

//    private lateinit var binding: ActivityShowConversationBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_show_conversation)
        conversationRecyclerView = findViewById(R.id.conversationRecyclerView)
        conversationRecyclerView.layoutManager = LinearLayoutManager(this)
//        conversationRecyclerView.adapter = ConversationAdapter(messages?.getMessages())

        champTxtMessage = findViewById(R.id.conversation_edtMessage)
        btnEnvoiMessage = findViewById(R.id.conversation_btnOK)

//        lvMessages = findViewById(R.id.conversation_lvMessages)
//        final messageAdapter = ArrayAdapter<String>(

        btnEnvoiMessage?.setOnClickListener(this)
        gs = application as GlobalState
        val bdl = this.intent.extras
        val convString = bdl?.getString("data") ?: ""
        hash = bdl?.getString("hash") ?: ""
        conv = Gson().fromJson(convString, Conversation::class.java)

        getConvMessagesRequest()

//        lvMessages?.adapter = arrayAdapter as ArrayAdapter<*>
//        arrayAdapter.clear()
//        arrayAdapter.notifyDataSetChanged()
//        arrayAdapter = ArrayAdapter<String>(
//            this,
//            android.R.layout.simple_list_item_1,
//            list
//        )
//        arrayAdapter2 = MessageAdapter()
        gs!!.alerter("data : " + (bdl?.getString("data") ?: ""))
        println(bdl)
//        svMessages = findViewById<View?>(R.id.conversation_svMessages) as ScrollView

    }

    override fun onResume() {
        super.onResume()
        conversationRecyclerView.adapter?.notifyDataSetChanged()
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
                    val messagesList = arrayListOf<String>()
                    for (m in messages?.getMessages()!!) {
                        if (m != null) {
                            messagesList.add(m.contenu!!)
                        }
                    }
                    println(messagesList)
//                    list = messagesList.toList().toTypedArray()
                    list.clear();
                    list.addAll(messagesList);
//                    arrayAdapter?.notify()
//                    notifyDataSetChanged();
                    println(list)
//                    list = messagesList
                    conversationRecyclerView.adapter = ConversationAdapter(messages?.getMessages())
                    conversationRecyclerView.adapter?.notifyDataSetChanged()

//                    lvMessages?.adapter = arrayAdapter as ArrayAdapter<*>
//                    arrayAdapter.clear()
//                    arrayAdapter.notifyDataSetChanged()

//                    arrayAdapter.swapData(messagesList)
//                    val a = ArrayAdapter(this, android.R.layout.simple_list_item_1, )
//                    val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, messagesList)
//                    var a = messagesList.toArray() as Array<*>
//                    var a = messagesList.toCollection(ArrayList())
//                    createAdapter(messagesList)
                }
            }

//            fun swapData(filtersList: List<String?>?) {
//                if (filtersList != null) {
//                    this.arrayList.clear()
//                    this.arrayList.addAll(filtersList)
//                    notifyDataSetChanged()
//                }
//            }

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
                Log.i("POST MESSAGE", res.toString())
            }
            override fun onFailure(call: Call<PostMessageResponse?>?, t: Throwable?) {
                call?.cancel()
            }
        })
    }

}