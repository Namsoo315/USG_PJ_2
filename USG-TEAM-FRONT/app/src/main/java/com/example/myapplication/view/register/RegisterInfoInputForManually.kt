package com.example.myapplication.view.register

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
import androidx.navigation.NavController
import com.example.myapplication.RegisterViewModel
import com.example.myapplication.data.BookItemIsbn
import com.example.myapplication.data.BookUsingIsbn

object RegisterInfoInputForManually {
    @Composable
    fun view(navController: NavController, registerViewModel: RegisterViewModel) {
        var bookTitle by remember { mutableStateOf(TextFieldValue("")) }
        var realPrice by remember { mutableStateOf(TextFieldValue("")) }
        var author by remember { mutableStateOf(TextFieldValue("")) }
        var publisher by remember { mutableStateOf(TextFieldValue("")) }

        Column {
            Text(text = "책 정보 등록")
            EditText(column = "책 제목", text = bookTitle, onTextChange = { bookTitle = it })
            EditText(column = "원가", text = realPrice, onTextChange = { realPrice = it })
            EditText(column = "저자", text = author, onTextChange = { author = it })
            EditText(column = "출판사", text = publisher, onTextChange = { publisher = it })

            Button(onClick = {
                // EditText에서 받은 값을 BookItemIsbn에 담습니다.
                registerViewModel.bookItemIsbn.value = BookUsingIsbn(bookName = bookTitle.text, author=author.text, publisher=publisher.text, bookRealPrice = realPrice.text)
                navController.navigate("registerInfoInputDetail")
            }) {
                Text("확인")
            }
        }
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    private fun EditText(column: String, text: TextFieldValue, onTextChange: (TextFieldValue) -> Unit) {
        Row {
            Text(column)
            TextField(
                value = text,
                onValueChange = onTextChange,
                singleLine = true,
            )
        }
    }
}
