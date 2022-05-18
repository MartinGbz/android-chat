package com.example.chat_2022_eleves

import retrofit2.Call
import retrofit2.http.*

interface APIInterface {
    @GET("conversations")
    fun doGetListConversation(@Header("hash") hash: String?): Call<ListConversations?>?

    @POST("authenticate")
    fun doPostAuthentication(@Header("Host") host: String, @Query("user") user: String, @Query("password") password: String) : Call<AuthenticationResponse?>?
}