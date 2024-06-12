package com.example.myapplication.view.main

import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.myapplication.api.ApiService
import com.example.myapplication.api.BookContent
import com.example.myapplication.api.BookResponse
import com.example.myapplication.view.auth.TokenManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object MainPageFragment{
    @Composable
    fun view(navController: NavHostController) {
        val bookList = remember { mutableStateListOf<BookContent>() }
        val currentPage = remember { mutableStateOf(1) }

        // LaunchedEffect will execute the given block when the component is first launched
        LaunchedEffect(key1 = true) {
            getBookList(currentPage.value, bookList)
        }
        Box{
            Column {
                TopSectionComponent.view(navController)
                BookListComponent.view(bookList, navController)
            }
            SellBookButtonComponent.SellBookButton(
                modifier = Modifier
                    .width(137.dp)
                    .height(56.dp)
                    .align(Alignment.BottomEnd),
                navController
            )
        }

    }

    private fun getBookList(page: Int, bookList: MutableList<BookContent>) {
        Log.d("hello", "getBookList 실행")
        val retrofit = Retrofit.Builder()
            .baseUrl("http://34.64.152.213:8080")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val apiService = retrofit.create(ApiService::class.java)
        val token = TokenManager.getToken()

        GlobalScope.launch(Dispatchers.IO) {
            if (token != null) {
                Log.d("hello", token)
            }
            val call = apiService.getBooks(page, token)

            call.enqueue(object : Callback<BookResponse> {
                override fun onResponse(call: Call<BookResponse>, response: Response<BookResponse>) {
                    if (response.isSuccessful) {
                        Log.d("hello", "getBookList 성공")
                        val bookResponse = response.body()
                        Log.d("getBookList", bookResponse.toString())
                        val fetchedBooks = bookResponse?.content ?: emptyList()
                        bookList.addAll(fetchedBooks)
                    } else {
                        // Handle error
                        val errorMessage = response.errorBody()?.string()
                        Log.d("hello", "getBookList failed: $errorMessage")
                    }
                }

                override fun onFailure(call: Call<BookResponse>, t: Throwable) {
                    // Handle failure
                    Log.d("hello", "getBookList failed, network error", t)
                }
            })
        }
    }
}