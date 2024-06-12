package com.example.myapplication.view.auth

import android.content.Context
import android.content.SharedPreferences

object TokenManager {
    private const val TOKEN_KEY = "token"
    private lateinit var sharedPreferences: SharedPreferences

    // 초기화 함수
    fun initialize(context: Context) {
        sharedPreferences = context.getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
    }

    // 토큰 저장
    fun saveToken(token: String) {
        sharedPreferences.edit().putString(TOKEN_KEY, token).apply()
    }

    // 토큰 가져오기
    fun getToken(): String? {
        return sharedPreferences.getString(TOKEN_KEY, null)
    }

    // 토큰 삭제
    fun deleteToken() {
        sharedPreferences.edit().remove(TOKEN_KEY).apply()
    }
}