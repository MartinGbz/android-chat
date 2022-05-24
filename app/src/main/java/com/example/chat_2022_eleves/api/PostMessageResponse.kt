package com.example.chat_2022_eleves.api

import com.example.chat_2022_eleves.model.Message

/**
 * Created by Martin Grabarz on 21/05/2022.
 */
class PostMessageResponse {
    var version: String? = null
    var success: String? = null
    var status: String? = null
    var message: Message? = null
    var id: String? = null
    var contenu: String? = null
    var auteur: String? = null
    var couleur: String? = null


}