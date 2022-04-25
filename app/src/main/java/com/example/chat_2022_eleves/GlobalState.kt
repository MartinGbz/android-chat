package com.example.chat_2022_eleves

import android.app.Application
import android.widget.Toast
import kotlin.Throws
import android.net.ConnectivityManager
import android.util.Log
import java.io.*
import java.lang.StringBuilder
import java.net.HttpURLConnection
import java.net.MalformedURLException
import java.net.URL
import android.content.Context
import android.net.NetworkCapabilities

class GlobalState : Application() {
    fun alerter(s: String?) {
        s ?: Log.i(CAT, s!!) // log si null
        val t = Toast.makeText(this, s, Toast.LENGTH_SHORT)
        t.show()
    }

    @Throws(IOException::class)
    private fun convertStreamToString(`in`: InputStream?): String? {
        return try {
            val reader = BufferedReader(InputStreamReader(`in`))
            val sb = StringBuilder()
            var line: String?
            while (reader.readLine().also { line = it } != null) {
                sb.append("""$line""".trimIndent())
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

    fun verifReseau(context: Context): Boolean {
        // On vérifie si le réseau est disponible, si oui on change le statut du bouton de connexion
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkCapabilities = connectivityManager.activeNetwork ?: return false
        val actNw = connectivityManager.getNetworkCapabilities(networkCapabilities) ?: return false
        var bStatut = true
        val sType: String = when {
            actNw.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> "Réseau wifi détecté"
            actNw.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> "Réseau mobile détecté"
            actNw.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> "Réseau ethernet détecté"
            else -> "Aucun réseau détecté"
        }
        alerter(sType)
        return bStatut
    }

    fun requeteGET(urlData: String?, qs: String?): String? {
        qs?.let {
            try {
                val url = URL("$urlData?$qs")
                Log.i(CAT, "url utilisée : $url")
                var urlConnection: HttpURLConnection?
                urlConnection = url.openConnection() as HttpURLConnection
                var `in`: InputStream?
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
        val dataout: DataOutputStream?
        if (qs != null) {
            try {
                val url = URL(urlData) // new:POST
                Log.i(CAT, "url utilisée : $url")
                var urlConnection: HttpURLConnection?
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
                var `in`: InputStream?
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
        const val CAT: String = "IG2I"
    }
}