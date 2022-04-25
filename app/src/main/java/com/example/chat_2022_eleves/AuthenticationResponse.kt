package com.example.chat_2022_eleves

import java.util.ArrayList

/**
 * Created by Martin Grabarz on 21/04/2022.
 */
class AuthenticationResponse {
    var version: String? = null
    var success: String? = null
    var status: String? = null
    var hash: String? = null

    // {"version":1.3,"success":true,"status":200,"conversations"
    override fun toString(): String {
        return "ListConversations{" +
                "version='" + version + '\'' +
                ", success='" + success + '\'' +
                ", status='" + status + '\'' +
                ", hash=" + hash +
                '}'
    }
}