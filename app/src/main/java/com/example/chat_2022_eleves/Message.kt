package com.example.chat_2022_eleves

class Message(id: String, contenu: String, auteur: String, couleur: String) {

    // {"version":1.3,"success":true,"status":200,"messages":
    //          [{"id":"625","contenu":"test","auteur":"castor","couleur":"black"}]
    // }

    var id: String? = null
    private var contenu: String? = null
    private var auteur: String? = null
    private var couleur: String? = null

    fun getContenu(): String? {
        return contenu
    }

    fun getAuteur(): String? {
        return auteur
    }

    fun getCouleur(): String? {
        return couleur
    }

}