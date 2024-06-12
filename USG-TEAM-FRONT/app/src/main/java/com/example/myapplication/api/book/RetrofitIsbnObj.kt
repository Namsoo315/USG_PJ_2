package com.example.myapplication.api.book

import com.google.gson.GsonBuilder
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.simplexml.SimpleXmlConverterFactory

object RetrofitIsbnObj {
    val gson = GsonBuilder()
        .setLenient()
        .create()

    val retrofit = Retrofit.Builder()
        .baseUrl("http://seoji.nl.go.kr")
        .addConverterFactory(GsonConverterFactory.create(gson))
        .build()


    val bookIsbnApi = retrofit.create(BookInterface::class.java)
}