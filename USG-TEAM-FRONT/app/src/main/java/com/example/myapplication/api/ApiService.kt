package com.example.myapplication.api

import com.google.gson.annotations.SerializedName
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Query
import java.util.Date

interface ApiService {
    @POST("/members/login")
    fun login(@Body loginRequest: LoginRequest): Call<LoginResponse>

    @POST("/members/join")
    fun join(@Body joinRequest: JoinRequest): Call<JoinResponse>

    @POST("/members/email")
    fun checkEmail(@Body emailCheckRequest: EmailCheckRequest): Call<EmailCheckResponse>

    @POST("/members/nickname")
    fun checkNickname(@Body nicknameCheckRequest: NicknameCheckRequest): Call<NicknameCheckResponse>

    @GET("/api/book")
    fun getBooks(@Query("page") page: Int, @Header("Authorization") token: String?): Call<BookResponse>
}

data class LoginRequest(val email: String, val password: String)
data class LoginResponse(val token: String)

data class JoinRequest(val email: String, val nickname: String, val password: String)
data class JoinResponse(val status: String)

data class EmailCheckRequest(val email: String)
data class EmailCheckResponse(val isAvailable: Boolean)

data class NicknameCheckRequest(val nickname: String)
data class NicknameCheckResponse(val isAvailable: Boolean)

data class BookResponse(
    @SerializedName("content")
    val content: List<BookContent>,
    val pageable: Pageable,
    val totalElements: Int,
    val totalPages: Int,
    val last: Boolean,
    val size: Int,
    val number: Int,
    val sort: Sort,
    val numberOfElements: Int,
    val first: Boolean,
    val empty: Boolean
)

data class BookContent(
    val book: Book,
    val image: List<String>
)

data class Book(
    val createdDate: String,
    val id: Int,
    val email: String,
    val bookName: String,
    val bookRealPrice: Int,
    val author: String,
    val publisher: String,
    val bookPostName: String,
    val bookComment: String,
    val bookPrice: Int,
    val isbn: String
)

data class Pageable(
    val pageNumber: Int,
    val pageSize: Int,
    val sort: Sort,
    val offset: Int,
    val paged: Boolean,
    val unpaged: Boolean
)

data class Sort(
    val empty: Boolean,
    val unsorted: Boolean,
    val sorted: Boolean
)