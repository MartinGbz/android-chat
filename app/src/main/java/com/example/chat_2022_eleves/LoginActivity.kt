package com.example.chat_2022_eleves

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
//import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.*
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import org.json.JSONObject
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
        // Nom du fichier des preferences par défaut : PACKAGE_NAME_preferences
        sp = this.getSharedPreferences("martin.grabarz.chat_2022_preferences", MODE_PRIVATE)
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
                }
            R.id.action_account -> gs?.alerter("Compte")
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onClick(view: View?) {
        val editor = sp?.edit()
        println(view)

        view?.let {
            when (view.id) {
                R.id.btnLogin -> {
                    manageLoginSaving(editor)
                    loginRequest()
                }
                else -> println("Unknown")
            }
        }
        editor?.apply()
    }


    /**
     * save login & password in preferences if the user ask for it
     */
    private fun manageLoginSaving(editor: SharedPreferences.Editor?) {
        if (cbRemember!!.isChecked) {
            modifyLoginPreferences(
                editor,
                true,
                edtLogin?.text.toString(),
                edtPasse?.text.toString()
            )
        } else {
            modifyLoginPreferences(editor, false, "", "")
        }
    }

    private fun loginRequest(): Unit? {
        val apiService = APIClient.getClient()?.create(APIInterface::class.java)
        val loginObject = JSONObject()
        loginObject.put("user", edtLogin?.text.toString())
        loginObject.put("password", edtPasse?.text.toString())
        val call1 = apiService?.doPostAuthentication(
            "tomnab.fr",
            edtLogin?.text.toString(),
            edtPasse?.text.toString()
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
                    println(edtLogin?.text.toString())
                    println(edtPasse?.text.toString())
                    versChoixConv.putExtras(bdl)
                    startActivity(versChoixConv)
                }
            }

            override fun onFailure(call: Call<AuthenticationResponse?>?, t: Throwable?) {
                call?.cancel()
            }
        })
    }

    private fun modifyLoginPreferences(editor: SharedPreferences.Editor?, remember: Boolean, login: String, password: String) {
        editor?.let {
            with(editor) {
                putBoolean("remember", remember)
                putString("login", login)
                putString("passe", password)
            }
        }
    }
}