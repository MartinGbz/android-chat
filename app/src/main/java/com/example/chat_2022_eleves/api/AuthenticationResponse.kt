package com.example.chat_2022_eleves.api

/**
 * Created by Martin Grabarz on 21/04/2022.
 */
class AuthenticationResponse {
    var version: String? = null
    var success: String? = null
    var status: String? = null
    var hash: String? = null

    override fun toString(): String {
        return "ListConversations{" +
                "version='" + version + '\'' +
                ", success='" + success + '\'' +
                ", status='" + status + '\'' +
                ", hash=" + hash +
                '}'
    }
}