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
import com.example.chat_2022_eleves.api.APIClient
import com.example.chat_2022_eleves.api.APIInterface
import com.example.chat_2022_eleves.model.Conversation
import com.example.chat_2022_eleves.model.ListConversations
import com.google.gson.Gson
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ChoixConvActivity : AppCompatActivity() {

    var gs: GlobalState? = null
    var listConversations : ListView? = null
    var hash: String? = null
    var pseudo: String? = null

    inner class MyCustomAdapter(
        context: Context?,
        private val layoutId: Int,
        private val dataConvs: ArrayList<Conversation?>?
    ) : ArrayAdapter<Conversation?>(context!!, layoutId, dataConvs!!) {

        override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View? {
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
            val inflater = layoutInflater
            val item = inflater.inflate(layoutId, parent, false)
            val nextC = dataConvs?.get(position)
            val label = item.findViewById<View?>(R.id.spinner_theme) as TextView
            label.text = nextC?.getTheme() ?: ""
            val icon = item.findViewById<View?>(R.id.spinner_icon) as ImageView
            if (nextC?.getActive() == true) icon.setImageResource(R.drawable.icon)
            else icon.setImageResource(R.drawable.icongray)
            listConversations?.setOnItemClickListener { parent, _, position, _ ->
                onClickListConv(parent, position)
            }
            return item
        }
    }

    private fun onClickListConv(parent: AdapterView<*>, position: Int) {
        val selectedItem = parent.getItemAtPosition(position) as Conversation
        print(selectedItem)
        val versAffichageConv = Intent(this@ChoixConvActivity, ConversationActivity::class.java)
        val bdl = Bundle()
        val convString = Gson().toJson(selectedItem)
        bdl.putString("data", convString)
        bdl.putString("hash", hash)
        bdl.putString("pseudo", pseudo)
        versAffichageConv.putExtras(bdl)
        startActivity(versAffichageConv)
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
                    R.layout.listview_item,
                    listeConvs?.getConversations()
                )
            }

            override fun onFailure(call: Call<ListConversations?>?, t: Throwable?) {
                call?.cancel()
            }
        })
    }
}