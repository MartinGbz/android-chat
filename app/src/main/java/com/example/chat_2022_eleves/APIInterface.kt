package com.example.chat_2022_eleves

import retrofit2.http.GET
import retrofit2.Call
import retrofit2.http.Header

interface APIInterface {
    @GET("conversations")
    fun doGetListConversation(@Header("hash") hash: String?): Call<ListConversations?>?
}