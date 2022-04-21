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

class LoginActivity : AppCompatActivity(), View.OnClickListener {
    var edtLogin: EditText? = null
    var edtPasse: EditText? = null
    var btnLogin: Button? = null
    var cbRemember: CheckBox? = null
    var sp: SharedPreferences? = null
    var gs: GlobalState? = null

    internal inner class JSONAsyncTask : AsyncTask<String?, Void?, JSONObject?>() {
        // Params, Progress, Result
        override fun onPreExecute() {
            super.onPreExecute()
            Log.i(GlobalState.Companion.CAT, "onPreExecute")
        }

        override fun doInBackground(vararg data: String?): JSONObject? {
            // pas d'interaction avec l'UI Thread ici
            // String... data : ellipse
            // data[0] contient le premier argument passé à .execute(...)
            // data[1] contient le second argument passé à .execute(...)

            // {"promo":"2020-2021",
            // "enseignants":[
            // {"prenom":"Mohamed","nom":"Boukadir"},
            // {"prenom":"Thomas","nom":"Bourdeaud'huy"},
            // {"prenom":"Mathieu","nom":"Haussher"},
            // {"prenom":"Slim","nom":"Hammadi"}]}
            Log.i(GlobalState.Companion.CAT, "doInBackground")
            val res = gs?.requeteGET(data[0], data[1])
            return try {
                JSONObject(res)
            } catch (e: JSONException) {
                e.printStackTrace()
                null
            }
        }

        override fun onPostExecute(result: JSONObject?) {
            Log.i(GlobalState.Companion.CAT, "onPostExecute")
            // parcourir le json reçu et afficher les enseignants
            gs?.alerter(result.toString())
            // Utiliser la librairie gson pour l'afficher
            val gson = GsonBuilder()
                .serializeNulls()
                .disableHtmlEscaping()
                .setPrettyPrinting()
                .create()
            gs?.alerter(gson.toJson(result))
            val p = gson.fromJson(result.toString(), Promo::class.java)
            gs?.alerter(p.toString())
            try {
                var s = ""
                val tabProfs = result?.getJSONArray("enseignants")
                if (tabProfs != null) {
                    for (i in 0 until tabProfs.length()) {
                        val nextProf = tabProfs.getJSONObject(i)
                        s += (nextProf.getString("prenom") + " "
                                + nextProf.getString("nom") + " ")
                    }
                }
                gs?.alerter(s)
            } catch (e: JSONException) {
                e.printStackTrace()
            }
        }
    }

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
        btnLogin?.setEnabled(gs!!.verifReseau())

        // relire les préférences de l'application
        // mettre à jour le formulaire
        if (sp!!.getBoolean("remember", false)) {
            // on charge automatiquement les champs login/passe
            // on coche la case
            edtLogin?.setText(sp!!.getString("login", ""))
            edtPasse?.setText(sp!!.getString("passe", ""))
            cbRemember?.setChecked(true)
        } else {
            // on vide
            edtLogin?.setText("")
            edtPasse?.setText("")
            cbRemember?.setChecked(false)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.getItemId()
        when (id) {
            R.id.action_settings -> {
                gs?.alerter("Préférences")

                // Changer d'activité pour afficher SettingsActivity
                val toSettings = Intent(this, SettingsActivity::class.java)
                startActivity(toSettings)
            }
            R.id.action_account -> gs?.alerter("Compte")
        }
        return super.onOptionsItemSelected(item)
    }

    internal inner class PostAsyncTask : AsyncTask<String?, Void?, String?>() {
        override fun onPreExecute() {
            super.onPreExecute()
        }

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

//    btnLogin.setOnClickListener(object : View.OnClickListener {
        //        override fun onClick(view: View?) {
        //            // Do some work here
        //        }
        override fun onClick(view: View?) {
            val editor = sp?.edit()
            if (view != null) {
                when (view.getId()) {
                    R.id.btnLogin -> {
                        // TODO : il faudrait sauvegarder les identifiants dans les préférences
                        gs?.alerter("click OK")
                        //gs.requeteGET("http://tomnab.fr/fixture/","");
                        //JSONAsyncTask reqGET = new JSONAsyncTask();
                        //reqGET.execute("http://tomnab.fr/fixture/","cle=valeur");

                        // http://tomnab.fr/chat-api/authenticate
                        val reqPOST = PostAsyncTask()
                        reqPOST.execute(
                            "http://tomnab.fr/chat-api/authenticate",
                            "user=" + edtLogin?.getText().toString()
                                    + "&password=" + edtPasse?.getText().toString()
                        )
                    }
                    R.id.cbRemember -> if (cbRemember!!.isChecked()) {
                        // on sauvegarde tout
                        if (editor != null) {
                            editor.putBoolean("remember", true)
                            editor.putString("login", edtLogin?.getText().toString())
                            editor.putString("passe", edtPasse?.getText().toString())
                        }
                    } else {
                        // on oublie tout
                        editor?.putBoolean("remember", false)
                        editor?.putString("login", "")
                        editor?.putString("passe", "")
                    }
                }
            }
            editor?.commit()
        }
//    })


}