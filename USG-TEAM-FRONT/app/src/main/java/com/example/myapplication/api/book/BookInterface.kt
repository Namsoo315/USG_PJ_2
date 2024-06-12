package com.example.myapplication.api.book

import com.example.myapplication.data.BookDetailResponse
import com.example.myapplication.data.PrePriceResponse
import com.example.myapplication.data.Root
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Part
import retrofit2.http.Path
import retrofit2.http.Query

interface BookInterface {
    @GET("landingPage/SearchApi.do")
    suspend fun getBookPrePriceIsbn(
        @Query("cert_key") key: String,
        @Query("page_no") pageNum: Int,
        @Query("result_style") resultStyle: String,
        @Query("page_size") pageSize: Int,
        @Query("isbn") isbnCode: String
    ): Response<PrePriceResponse>


    @GET("api/book/{bookId}")
    fun getBookItem(
        @Path("bookId") bookId: Int
    ): Call<BookDetailResponse>

    @Multipart
    @POST("api/book")
    fun uploadBook(
        @Header("Authorization") accessToken: String,
        @Part("bookName") bookName: RequestBody,
        @Part("bookComment") bookComment: RequestBody,
        @Part("bookPostName") bookPostName: RequestBody,
        @Part("bookPrice") bookPrice: RequestBody,
        @Part("isbn") isbn: RequestBody,
        @Part("bookRealPrice") bookRealPrice: RequestBody,
        @Part("author") author: RequestBody,
        @Part("publisher") publisher: RequestBody,
        @Part images: List<MultipartBody.Part>
    ): Call<ResponseBody>

    @Multipart
    @PUT("api/book/{bookId}")
    fun modifyBook(
        @Path("bookId") bookId: Int,
        @Part("bookComment") bookComment: RequestBody,
        @Part("bookPostName") bookPostName: RequestBody,
        @Part("bookPrice") bookPrice: RequestBody,
        @Part images: List<MultipartBody.Part>
    ): Call<ResponseBody>

    @DELETE("/api/book/{bookId}")
    fun deleteBook(
        @Header("Authorization") accessToken: String,
        @Path("bookId") bookId: Int
    ): Call<ResponseBody>
}