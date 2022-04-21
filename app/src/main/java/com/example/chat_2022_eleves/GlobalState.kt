package com.example.chat_2022_eleves

import android.app.Application
import android.widget.Toast
import kotlin.Throws
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.util.Log
import java.io.*
import java.lang.StringBuilder
import java.net.HttpURLConnection
import java.net.MalformedURLException
import java.net.URL

class GlobalState : Application() {
    fun alerter(s: String?) {
        if (s != null) {
            Log.i(CAT, s)
        }
        val t = Toast.makeText(this, s, Toast.LENGTH_SHORT)
        t.show()
    }

    @Throws(IOException::class)
    private fun convertStreamToString(`in`: InputStream?): String? {
        return try {
            val reader = BufferedReader(InputStreamReader(`in`))
            val sb = StringBuilder()
            var line: String? = null
            while (reader.readLine().also { line = it } != null) {
                sb.append(
                    """
    $line
    
    """.trimIndent()
                )
            }
            sb.toString()
        } finally {
            try {
                `in`?.close()
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }

    fun verifReseau(): Boolean {
        // On vérifie si le réseau est disponible,
        // si oui on change le statut du bouton de connexion
        val cnMngr = getSystemService(CONNECTIVITY_SERVICE) as ConnectivityManager
        val netInfo = cnMngr.activeNetworkInfo
        var sType = "Aucun réseau détecté"
        var bStatut = false
        if (netInfo != null) {
            val netState = netInfo.state
            if (netState.compareTo(NetworkInfo.State.CONNECTED) == 0) {
                bStatut = true
                val netType = netInfo.type
                when (netType) {
                    ConnectivityManager.TYPE_MOBILE -> sType = "Réseau mobile détecté"
                    ConnectivityManager.TYPE_WIFI -> sType = "Réseau wifi détecté"
                }
            }
        }
        alerter(sType)
        return bStatut
    }

    fun requeteGET(urlData: String?, qs: String?): String? {
        if (qs != null) {
            try {
                val url = URL("$urlData?$qs")
                Log.i(CAT, "url utilisée : $url")
                var urlConnection: HttpURLConnection? = null
                urlConnection = url.openConnection() as HttpURLConnection
                var `in`: InputStream? = null
                `in` = BufferedInputStream(urlConnection.inputStream)
                val txtReponse = convertStreamToString(`in`)
                urlConnection.disconnect()
                return txtReponse
            } catch (e: MalformedURLException) {
                e.printStackTrace()
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
        return ""
    }

    fun requetePOST(urlData: String?, qs: String?): String? {
        var dataout: DataOutputStream? = null // new:POST
        if (qs != null) {
            try {
                val url = URL(urlData) // new:POST
                Log.i(CAT, "url utilisée : $url")
                var urlConnection: HttpURLConnection? = null
                urlConnection = url.openConnection() as HttpURLConnection

                // new:POST
                urlConnection.requestMethod = "POST"
                urlConnection.doOutput = true
                urlConnection.doInput = true
                urlConnection.useCaches = false
                urlConnection.allowUserInteraction = false
                urlConnection.setRequestProperty(
                    "Content-Type",
                    "application/x-www-form-urlencoded"
                )
                dataout = DataOutputStream(urlConnection.outputStream)
                dataout.writeBytes(qs)
                // new:POST
                var `in`: InputStream? = null
                `in` = BufferedInputStream(urlConnection.inputStream)
                val txtReponse = convertStreamToString(`in`)
                urlConnection.disconnect()
                return txtReponse
            } catch (e: MalformedURLException) {
                e.printStackTrace()
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
        return ""
    }

    companion object {
        val CAT: String? = "IG2I"
    }
}