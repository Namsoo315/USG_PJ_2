package com.example.myapplication.view.modify

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.Button
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.text.input.TextFieldValue
import androidx.navigation.NavController
import coil.compose.rememberImagePainter
import com.example.myapplication.ModifyViewModel
import com.example.myapplication.api.book.BookApi
import com.example.myapplication.data.BookItem

object ModifyPageFragment {
    val bookApi = BookApi()

    @Composable
    fun view(modifyViewModel: ModifyViewModel, navController: NavController) {

        val book: BookItem? by modifyViewModel.bookItem.observeAsState()

        var bookComment by remember { mutableStateOf(TextFieldValue(book?.bookComment ?: "")) }
        var bookPostName by remember { mutableStateOf(TextFieldValue(book?.bookPostName ?: "")) }
        var bookPrice by remember { mutableStateOf(TextFieldValue(book?.bookPrice.toString() ?: "")) }

        var imageUrisState by remember { mutableStateOf(listOf<Uri>()) }

        Column {
            bookPostName?.let { TitleTextField(text = it, onTextChange = { bookPostName = it }) }
            GalleryMultipleImagePicker(imageUrisState = imageUrisState) { uris ->
                imageUrisState = uris
            }

            bookPrice?.let { InfoDetailLine(column = "판매가", text = it, onTextChange = { bookPrice = it }) }
            bookComment?.let { InfoDetailLine(column = "상세정보", text = it, onTextChange = { bookComment = it }) }
            Button(onClick = {
//                bookApi.modifyBook(
//                    bookComment = bookComment.text,
//                    bookPostName = bookPostName.text,
//                    bookPrice = bookPrice.text.toInt(),
//                    imageUris = imageUrisState,
//                    bookId = modifyViewModel.bookId
//                )
                navController.navigate("main")
            }) {
                Text("확인")
            }
        }
    }

    @Composable
    fun GalleryMultipleImagePicker(imageUrisState: List<Uri>, onUrisSelected: (List<Uri>) -> Unit) {

        val launcher = rememberLauncherForActivityResult(contract = ActivityResultContracts.GetMultipleContents()) { uris: List<Uri> ->
            onUrisSelected(uris) // 여기에서 콜백을 호출하여 URI 리스트를 업데이트합니다.
        }

        Button(onClick = { launcher.launch("image/*") }) {
            Text("Open Gallery")
        }

        Row {
            imageUrisState.forEach { uri ->
                Image(painter = rememberImagePainter(data = uri), contentDescription = null)
            }
        }
    }

    @Composable
    fun InfoForIsbnLine(title: String, content: String) = Row {
        Text(text = title)
        InfoForIsbnText(text = content)
    }

    @Composable
    private fun InfoForIsbnText(text: String) {
        Text(text)
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    private fun TitleTextField(text: TextFieldValue, onTextChange: (TextFieldValue) -> Unit) {
        TextField(
            value = text,
            onValueChange = onTextChange,
            singleLine = true,
            placeholder = { Text("제목을 입력하시오") }
        )
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    private fun InfoDetailLine(column: String, text: TextFieldValue, onTextChange: (TextFieldValue) -> Unit) = Row {
        Text(column)
        TextField(
            value = text,
            onValueChange = onTextChange
        )
    }

}