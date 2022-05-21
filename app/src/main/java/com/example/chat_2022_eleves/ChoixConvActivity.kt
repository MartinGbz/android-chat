package com.example.chat_2022_eleves

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.google.gson.Gson
import kotlinx.serialization.json.JsonObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.lang.ProcessBuilder.Redirect.to

class ChoixConvActivity : AppCompatActivity(), View.OnClickListener {

    var gs: GlobalState? = null
    var spinConversations: Spinner? = null
    var btnChoixConv: Button? = null

    inner class MyCustomAdapter(
        context: Context?,
        private val layoutId: Int,
        private val dataConvs: ArrayList<Conversation?>?
    ) : ArrayAdapter<Conversation?>(context!!, layoutId, dataConvs!!) {

        override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View? {
            // return getCustomView(position, convertView, parent);
            // getLayoutInflater() vient de Android.Activity => il faut utiliser une classe interne
            val inflater = layoutInflater
            val item = inflater.inflate(layoutId, parent, false)
            val nextC = dataConvs?.get(position)
            val label = item.findViewById<View?>(R.id.spinner_theme) as TextView
            label.text = nextC?.getTheme() ?: ""
            val icon = item.findViewById<View?>(R.id.spinner_icon) as ImageView
            if (nextC?.getActive() == true) {
                icon.setImageResource(R.drawable.icon36)
            }
            else {
                icon.setImageResource(R.drawable.icongray36)
            }
            return item
        }

        @SuppressLint("ViewHolder")
        override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
            // return getCustomView(position, convertView, parent);
            val inflater = layoutInflater
            val item = inflater.inflate(layoutId, parent, false)
            val nextC = dataConvs?.get(position)
            val label = item.findViewById<View?>(R.id.spinner_theme) as TextView
            label.text = nextC?.getTheme() ?: ""
            val icon = item.findViewById<View?>(R.id.spinner_icon) as ImageView
            if (nextC?.getActive() == true) icon.setImageResource(R.drawable.icon)
            else icon.setImageResource(R.drawable.icongray)
            return item
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_choix_conversation)
        btnChoixConv = findViewById(R.id.btnChoixConv)
        btnChoixConv?.setOnClickListener(this)
        gs = application as GlobalState
        val bdl = this.intent.extras
        gs!!.alerter("hash : " + (bdl?.getString("hash") ?: ""))
        val hash = bdl?.getString("hash")
        val apiService = APIClient.getClient()?.create(APIInterface::class.java)
        val call1 = apiService?.doGetListConversation(hash)
        println("bdl")
        println(bdl)
        println("hash")
        println(hash)
        println("apiService")
        println(apiService)
        println("call1")
        println(call1)
        call1?.enqueue(object : Callback<ListConversations?> {
            override fun onResponse(
                call: Call<ListConversations?>?,
                response: Response<ListConversations?>?
            ) {
                val listeConvs = response?.body()
                Log.i(GlobalState.CAT, listeConvs.toString())
                spinConversations = findViewById<View?>(R.id.spinConversations) as Spinner
                spinConversations!!.adapter = MyCustomAdapter(
                    this@ChoixConvActivity,
                    R.layout.spinner_item,
                    listeConvs?.getConversations()
                )
            }

            override fun onFailure(call: Call<ListConversations?>?, t: Throwable?) {
                call?.cancel()
            }
        })
    }

    override fun onClick(view: View?) {
        // println(view)
        view?.let {
            when (view.id) {
                R.id.btnChoixConv -> {
                    // gs!!.alerter("Btn choix cliqué")
                    val versAffichageConv = Intent(this@ChoixConvActivity, ConversationActivity::class.java)
                    val bdl = Bundle()
                    val conv: Conversation = spinConversations?.selectedItem as Conversation
                    val b = spinConversations?.selectedItem.toString()
                    val test = Gson().toJson(conv)
                    val test2 = Gson().fromJson(test, Conversation::class.java)
//                    val jObjectInstance = JsonObject()
//                    jObjectInstance.addProperty("name", conv.id);
//                    val jsonList = Json.encodeToString(a);
//                    Conversation a =
//                    val jsonList = Json.stringify(Conversation, spinConversations?.selectedItem);
//                    val q = Json.encodeToString(spinConversations?.selectedItem)
                    bdl.putString("data", test)
                    versAffichageConv.putExtras(bdl)
                    startActivity(versAffichageConv)
                }
                else -> println("Unknown")
            }
        }
    }
}