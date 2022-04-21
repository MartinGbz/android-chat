package com.example.chat_2022_eleves

import android.content.Context
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.ViewGroup
import android.view.View
import android.widget.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.ArrayList

class ChoixConvActivity : AppCompatActivity() {
    var gs: GlobalState? = null
    var spinConversations: Spinner? = null

    inner class MyCustomAdapter(
        context: Context?,
        private val layoutId: Int,
        private val dataConvs: ArrayList<Conversation?>?
    ) : ArrayAdapter<Conversation?>(context!!, layoutId, dataConvs!!) {
        override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup?): View? {
            //return getCustomView(position, convertView, parent);
            // getLayoutInflater() vient de Android.Activity => il faut utiliser une classe interne
            val inflater = layoutInflater
            val item = inflater.inflate(layoutId, parent, false)
            val nextC = dataConvs?.get(position)
            val label = item.findViewById<View?>(R.id.spinner_theme) as TextView
            label.text = nextC?.getTheme() ?: ""
            val icon = item.findViewById<View?>(R.id.spinner_icon) as ImageView
            if (nextC?.getActive() == true) icon.setImageResource(R.drawable.icon36) else icon.setImageResource(
                R.drawable.icongray36
            )
            return item
        }

        override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
            //return getCustomView(position, convertView, parent);
            val inflater = layoutInflater
            val item = inflater.inflate(layoutId, parent, false)
            val nextC = dataConvs?.get(position)
            val label = item.findViewById<View?>(R.id.spinner_theme) as TextView
            label.text = nextC?.getTheme() ?: ""
            val icon = item.findViewById<View?>(R.id.spinner_icon) as ImageView
            if (nextC?.getActive() == true) icon.setImageResource(R.drawable.icon) else icon.setImageResource(
                R.drawable.icongray
            )
            return item
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_choix_conversation)
        gs = application as GlobalState
        val bdl = this.intent.extras
        gs!!.alerter("hash : " + (bdl?.getString("hash") ?: ""))
        val hash = bdl?.getString("hash")
        val apiService = APIClient.getClient()?.create(APIInterface::class.java)
        val call1 = apiService?.doGetListConversation(hash)
        call1?.enqueue(object : Callback<ListConversations?> {
            override fun onResponse(
                call: Call<ListConversations?>?,
                response: Response<ListConversations?>?
            ) {
                val listeConvs = response?.body()
                Log.i(GlobalState.Companion.CAT, listeConvs.toString())
                spinConversations = findViewById<View?>(R.id.spinConversations) as Spinner
                //ArrayAdapter<Conversation> dataAdapter = new ArrayAdapter<Conversation>(
                //        ChoixConversationActivity.this,
                //        android.R.layout.simple_spinner_item,
                //        listeConvs.getConversations());
                //dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                //spinConversations.setAdapter(dataAdapter);
                spinConversations!!.setAdapter(
                    MyCustomAdapter(
                        this@ChoixConvActivity,
                        R.layout.spinner_item,
                        listeConvs?.getConversations()
                    )
                )
            }

            override fun onFailure(call: Call<ListConversations?>?, t: Throwable?) {
                call?.cancel()
            }
        })
    }
}