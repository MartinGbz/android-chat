package com.example.chat_2022_eleves

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
//import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.google.gson.Gson
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ChoixConvActivity : AppCompatActivity() {

    var gs: GlobalState? = null
//    var spinConversations: Spinner? = null
    var listConversations : ListView? = null
//    var btnChoixConv: Button? = null
    var hash: String? = null
    var pseudo: String? = null

    inner class MyCustomAdapter(
        context: Context?,
        private val layoutId: Int,
        private val dataConvs: ArrayList<Conversation?>?,
        hash: String ,
        pseudo: String
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
            listConversations?.setOnItemClickListener { parent, _, position, _ ->
                val selectedItem = parent.getItemAtPosition(position) as Conversation
                print(selectedItem)
                val versAffichageConv = Intent(this@ChoixConvActivity, ConversationActivity::class.java)
                val bdl = Bundle()
//                val conv: Conversation = listConversations?.selectedItem as Conversation
                val convString = Gson().toJson(selectedItem)
                bdl.putString("data", convString)
                bdl.putString("hash", hash)
                bdl.putString("pseudo", pseudo)
                versAffichageConv.putExtras(bdl)
                startActivity(versAffichageConv)
            }
            return item
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_choix_conversation)
        gs = application as GlobalState
        val bdl = this.intent.extras
        gs!!.alerter("hash : " + (bdl?.getString("hash") ?: ""))
        this.hash = bdl?.getString("hash")
        this.pseudo = bdl?.getString("pseudo")
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
                listConversations = findViewById<View?>(R.id.listConversations) as ListView
                listConversations!!.adapter = MyCustomAdapter(
                    this@ChoixConvActivity,
                    R.layout.spinner_item,
                    listeConvs?.getConversations(),
                    hash!!,
                    pseudo!!,
                )
//                spinConversations = findViewById<View?>(R.id.spinConversations) as Spinner
//                spinConversations!!.adapter = MyCustomAdapter(
//                    this@ChoixConvActivity,
//                    R.layout.spinner_item,
//                    listeConvs?.getConversations()
//                )
            }

            override fun onFailure(call: Call<ListConversations?>?, t: Throwable?) {
                call?.cancel()
            }
        })
    }

//    override fun onClick(view: View?) {
//        // println(view)
//        view?.let {
//            when (view.id) {
//                R.id.listConversations -> {
//                    // gs!!.alerter("Btn choix cliqué")
//                    val versAffichageConv = Intent(this@ChoixConvActivity, ConversationActivity::class.java)
//                    val bdl = Bundle()
////                    val conv: Conversation = spinConversations?.selectedItem as Conversation
//                    val conv: Conversation = listConversations?.selectedItem as Conversation
//                    val convString = Gson().toJson(conv)
//                    bdl.putString("data", convString)
//                    bdl.putString("hash", this.hash)
//                    bdl.putString("pseudo", this.pseudo)
//                    versAffichageConv.putExtras(bdl)
//                    startActivity(versAffichageConv)
//                }
//                else -> println("Unknown")
//            }
//        }
//    }
}