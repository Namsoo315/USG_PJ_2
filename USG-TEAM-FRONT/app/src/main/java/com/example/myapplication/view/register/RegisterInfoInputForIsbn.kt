package com.example.myapplication.view.register

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.text.input.TextFieldValue
import androidx.navigation.NavHostController
import androidx.compose.runtime.rememberCoroutineScope
import com.example.myapplication.BuildConfig
import com.example.myapplication.RegisterViewModel
import com.example.myapplication.api.book.RetrofitIsbnObj.bookIsbnApi
import com.example.myapplication.data.BookUsingIsbn
import kotlinx.coroutines.launch


object RegisterInfoInputForIsbn {
    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun view(navController: NavHostController, registerViewModel: RegisterViewModel) {
        var isbn by remember { mutableStateOf(TextFieldValue("")) }
        val coroutineScope = rememberCoroutineScope()

        Column {
            Text("책 정보 등록")
            Row {
                Text("ISBN")
                TextField(
                    value = isbn,
                    onValueChange = { newText -> isbn = newText },
                    singleLine = true,
                )
            }
            Button(onClick = {
                coroutineScope.launch {
                    val bookItemIsbn = getInfoForIsbn(isbn.text)
                    if (bookItemIsbn != null) {
                        // BookItemIsbn 객체를 ViewModel의 bookItemIsbn 속성에 저장합니다.
                        registerViewModel.bookItemIsbn.value = bookItemIsbn
                        registerViewModel.isbnCode = isbn.text
                        navController.navigate("registerInfoInputDetail")
                    }
                }
            }) {
                Text("확인")
            }
        }
    }

    private suspend fun getInfoForIsbn(isbnCode: String): BookUsingIsbn? {
        val key = BuildConfig.API_KEY

        try {
            val bookItemResponce = bookIsbnApi.getBookPrePriceIsbn(key, 1, "json", 1, isbnCode)
            Log.d("isbn", bookItemResponce.body().toString())

            if (bookItemResponce.isSuccessful) {
                val docs = bookItemResponce.body()?.docs
                if (!docs.isNullOrEmpty()) {
                    val bookItems = docs[0]
                    // bookItems에서 필요한 정보를 가져와서 BookUsingIsbn 객체를 생성
                    val bookUsingIsbn = BookUsingIsbn(
                        bookName = bookItems.TITLE,
                        author = bookItems.AUTHOR,
                        publisher = bookItems.PUBLISHER,
                        bookRealPrice = bookItems.PRE_PRICE
                    )
                    return bookUsingIsbn
                } else {
                    Log.d("getInfoForIsbn", "통신 성공, 하지만 docs가 비어 있음")
                    return null
                }
            } else {
                Log.d("getInfoForIsbn", "통신 실패: ${bookItemResponce.errorBody()?.string()}")
                return null
            }

        } catch (e: Exception) {
            println("API 요청 중 오류 발생: ${e.message}")
            return null
        }
    }



}