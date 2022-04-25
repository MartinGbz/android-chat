package com.example.chat_2022_eleves

import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.*

interface APIInterface {
    @GET("conversations")
    open fun doGetListConversation(@Header("hash") hash: String?): Call<ListConversations?>?

    @POST("authenticate")
    open fun doPostAuthentication(@Header("Host") host: String, @Query("user") user: String, @Query("password") password: String) : Call<AuthenticationResponse?>?
}