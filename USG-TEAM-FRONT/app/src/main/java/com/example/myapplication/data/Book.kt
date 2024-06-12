package com.example.myapplication.data

data class BookDetailResponse(
    val data: BookItem,
    val message: String
)

data class BookItem(
    val bookName: String,
    val bookComment: String,
    val bookPostName: String,
    val bookPrice: Int,
    val bookRealPrice: Int,
    val nickname: String?,
    val imageUrls: List<String>,
    val author: String,
    val publisher: String
)

