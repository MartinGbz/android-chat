package com.example.chat_2022_eleves

import com.google.gson.GsonBuilder
import android.support.v7.app.AppCompatActivity
import android.content.SharedPreferences
import android.os.AsyncTask
import org.json.JSONObject
import org.json.JSONException
import android.os.Bundle
import android.preference.PreferenceManager
import android.content.Intent
import android.util.Log
import android.view.*
import android.widget.*
import kotlinx.coroutines.runBlocking
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginActivity : AppCompatActivity(), View.OnClickListener {
    var edtLogin: EditText? = null
    var edtPasse: EditText? = null
    var btnLogin: Button? = null
    var cbRemember: CheckBox? = null
    var sp: SharedPreferences? = null
    var gs: GlobalState? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        edtLogin = findViewById(R.id.edtLogin)
        edtPasse = findViewById(R.id.edtPasse)
        btnLogin = findViewById(R.id.btnLogin)
        cbRemember = findViewById(R.id.cbRemember)
        sp = PreferenceManager.getDefaultSharedPreferences(this)
        btnLogin?.setOnClickListener(this)
        cbRemember?.setOnClickListener(this)
        gs = application as GlobalState
    }

    override fun onStart() {
        super.onStart()

        // Si le réseau est disponible, alors on réactive le bouton OK
        btnLogin?.isEnabled = gs!!.verifReseau(this)

        // relire les préférences de l'application
        // mettre à jour le formulaire
        if (sp!!.getBoolean("remember", false)) {
            // on charge automatiquement les champs login/passe
            // on coche la case
            edtLogin?.setText(sp!!.getString("login", ""))
            edtPasse?.setText(sp!!.getString("passe", ""))
            cbRemember?.isChecked = true
        } else {
            // on vide
            edtLogin?.setText("")
            edtPasse?.setText("")
            cbRemember?.isChecked = false
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId
        when (id) {
            R.id.action_settings -> {
                    gs?.alerter("Préférences")

                    // Changer d'activité pour afficher SettingsActivity
                    val toSettings = Intent(this, SettingsActivity::class.java)
                    startActivity(toSettings)
                    /*
                    if (fragment == null) return
                    val fm = supportFragmentManager
                    val tr = fm.beginTransaction()
                    tr.add(R.id.framlayout, fragment)
                    tr.commitAllowingStateLoss()
                    curFragment = fragment*/

                }
            R.id.action_account -> gs?.alerter("Compte")
        }
        return super.onOptionsItemSelected(item)
    }

    internal inner class PostAsyncTask : AsyncTask<String?, Void?, String?>() {

        override fun doInBackground(vararg data: String?): String? {
            val res = gs?.requetePOST(data[0], data[1])
            // {"version":1.3,"success":true,
            // "status":202,"hash":"efd18c70f94a580d9dc85533ddcd9823"}
            return try {
                val ob = JSONObject(res)
                ob.getString("hash")
            } catch (e: JSONException) {
                e.printStackTrace()
                ""
            }
        }

        override fun onPostExecute(hash: String?) {
            // changer d'activité => ChoixConversations
            if (hash === "") return
            val versChoixConv = Intent(this@LoginActivity, ChoixConvActivity::class.java)
            val bdl = Bundle()
            bdl.putString("hash", hash)
            versChoixConv.putExtras(bdl)
            startActivity(versChoixConv)
        }
    }

    override fun onClick(view: View?) {
        val editor = sp?.edit()
        println(view)

        view?.let {
            when (view!!.id) {
                R.id.btnLogin -> {
                    // TODO : il faudrait sauvegarder les identifiants dans les préférences
                    gs?.alerter("click OK")
                    //gs.requeteGET("http://tomnab.fr/fixture/","");
                    //JSONAsyncTask reqGET = new JSONAsyncTask();
                    //reqGET.execute("http://tomnab.fr/fixture/","cle=valeur");

                    // http://tomnab.fr/chat-api/authenticate

//                    val reqPOST = PostAsyncTask()
//                    reqPOST.execute(
//                        "http://tomnab.fr/chat-api/authenticate",
//                        "user=" + edtLogin?.text.toString() +
//                                "&password=" + edtPasse?.text.toString()
//                    )

                    val apiService = APIClient.getClient()?.create(APIInterface::class.java)
                    val loginObject = JSONObject()
                    loginObject.put("user", edtLogin?.text.toString())
                    loginObject.put("password", edtPasse?.text.toString())
//                    val call1 = apiService?.doPostAuthentication(loginObject.toString().toRequestBody())
                    val call1 = apiService?.doPostAuthentication("tomnab.fr",edtLogin?.text.toString(), edtPasse?.text.toString())
                    call1?.enqueue(object : Callback<AuthenticationResponse?> {
                        override fun onResponse(
                            call: Call<AuthenticationResponse?>?,
                            response: Response<AuthenticationResponse?>?
                        ) {
                            println("LOGIN WOOOOOORKED")
                            val res = response?.body()
//                            println(response)
//                            println(call1)
//                            println(call1.isCanceled)
//                            println(call1.isExecuted)
//                            println(call1.toString())
//                            println(call)
//                            println(call.toString())
//                            println(res)
                            Log.i(GlobalState.Companion.CAT, res.toString())
                            if (res?.success.toBoolean() == true) {
                                if (res?.hash === "") return
                                val versChoixConv = Intent(this@LoginActivity, ChoixConvActivity::class.java)
                                val bdl = Bundle()
                                bdl.putString("hash", res?.hash)
                                versChoixConv.putExtras(bdl)
                                startActivity(versChoixConv)
                            }
                        }

                        override fun onFailure(
                            call: Call<AuthenticationResponse?>?,
                            t: Throwable?
                        ) {
                            call?.cancel()
                        }
                    })
                }
                R.id.cbRemember -> {
                    if (cbRemember!!.isChecked) {
                        editor?.let {
                            editor!!.putBoolean("remember", true)
                            editor.putString("login", edtLogin?.text.toString())
                            editor.putString("passe", edtPasse?.text.toString())
                        }
                    }
                    else {
                        editor?.putBoolean("remember", false)
                        editor?.putString("login", "")
                        editor?.putString("passe", "")
                    }
                }
                else -> println("Unknown")
            }
        }
        editor?.commit()
    }
}