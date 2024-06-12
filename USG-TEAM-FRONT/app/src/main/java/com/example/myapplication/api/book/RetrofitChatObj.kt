package com.example.myapplication.api.book

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitChatObj {

    private const val BASE_URL: String = "http://34.64.152.213:8081/"
    val retrofitChat: Retrofit.Builder by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
    }
}