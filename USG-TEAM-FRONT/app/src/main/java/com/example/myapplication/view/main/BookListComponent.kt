package com.example.myapplication.view.main

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.Divider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.rememberImagePainter
import com.example.myapplication.R
import com.example.myapplication.api.BookContent


object BookListComponent {
    @Composable
    fun view(books: SnapshotStateList<BookContent>, navController: NavController) {
        BookList(books, navController)
    }

    @Composable
    private fun BookList(books: List<BookContent>, navController: NavController) = LazyColumn(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight()
    ) {
        itemsIndexed(books) { index, bookContent ->
            Column(
                modifier = Modifier
                    .clickable {
                        navController.navigate("detail/${bookContent.book.id}/${bookContent.book.email}")
                    }
            ) {
                Row {
                    val imageUrl = bookContent.image.getOrElse(0) { "" } // 이미지가 없는 경우 빈 문자열을 반환합니다.

                    if (imageUrl.isNotEmpty()) {
                        BookImage(imageUrl)
                    }
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(22.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text(text = bookContent.book.bookPostName)
                        Text(text = bookContent.book.bookName)
                        Text(text = "${bookContent.book.bookPrice} / ${bookContent.book.bookRealPrice}")
                        Text(text = "${bookContent.book.publisher}, ${bookContent.book.author}")
                    }
                }
                Divider(
                    modifier = Modifier.padding(top = 30.dp, bottom = 30.dp, start = 20.dp, end = 20.dp),
                    color = Color.Black
                )
            }
        }
    }

    @Composable
    private fun BookImage(imageUrl: String) {
        val painter = rememberImagePainter(imageUrl)

        Image(
            painter = painter,
            contentDescription = "이미지 설명",
            modifier = Modifier
                .width(48.dp)
                .height(48.dp),
            contentScale = ContentScale.Crop
        )
    }

    @Composable
    private fun ContentText(text: String) = Text(
        text = text,
        fontSize = 20.sp
    )
}