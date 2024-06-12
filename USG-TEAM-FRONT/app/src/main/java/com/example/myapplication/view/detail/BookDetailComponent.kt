package com.example.myapplication.view.detail

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ModalBottomSheetLayout
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberImagePainter
import com.example.myapplication.R
import com.example.myapplication.data.BookItem
import com.example.myapplication.view.component.IconButtonComponent.IconButton
import kotlinx.coroutines.launch
import androidx.compose.material.rememberModalBottomSheetState
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.runtime.State
import androidx.compose.runtime.livedata.observeAsState
import androidx.lifecycle.LiveData
import androidx.navigation.NavController
import com.example.myapplication.ModifyViewModel
import com.example.myapplication.api.book.BookApi
import com.example.myapplication.api.book.RetrofitChatObj
import com.example.myapplication.data.BookDetailResponse
import com.example.myapplication.view.auth.TokenManager
import com.example.myapplication.view.chat.ChatRequest
import com.example.myapplication.view.chat.ChatService


object BookDetailComponent {
    val bookApi = BookApi()
    val chatApi: ChatService by lazy {
        RetrofitChatObj.retrofitChat.build().create(ChatService::class.java)
    }

    @Composable
    fun view(bookId: Int, email: String?, modifyViewModel: ModifyViewModel, navController: NavController) {
        val bookDetailResponse: LiveData<BookDetailResponse> = bookApi.getBookDetail(bookId)
        val bookDetailState: State<BookDetailResponse?> = bookDetailResponse.observeAsState(null)

        when {
            bookDetailState.value != null ->  {
                val item = bookDetailState.value?.data ?: BookItem(
                    bookPostName = "",
                    nickname = "",
                    imageUrls = listOf(),
                    bookName = "",
                    bookPrice = 0,
                    bookRealPrice = 0,
                    author = "",
                    publisher = "",
                    bookComment = ""
                )
                BottomSheetMenu(item, modifyViewModel, navController, bookId, email)
            }
        }
    }

    @Composable
    private fun PostTitleText(text: String) = Text(
        text = text,
        fontSize = 30.sp
    )
    @OptIn(ExperimentalMaterialApi::class)
    @Composable
    fun BottomSheetMenu(
        bookItem: BookItem,
        modifyViewModel: ModifyViewModel,
        navController: NavController,
        bookId: Int,
        email: String?
    ) {
        val coroutineScope = rememberCoroutineScope()
        val sheetState = rememberModalBottomSheetState(initialValue = ModalBottomSheetValue.Hidden)
        ModalBottomSheetLayout(
            sheetState = sheetState,
            sheetContent = {
                Column {
                    Button(onClick = {
                        modifyViewModel.bookItem.value = bookItem // ModifyViewModel의 bookItem을 설정합니다.
                        navController.navigate("modifyBookInfo") // ModifyPageFragment로 이동합니다.
                    }) {
                        Text("수정하기")
                    }
                    Button(onClick = {
                        bookApi.deleteBook(bookId)
                        navController.navigate("main")
                    }) {
                        Text("삭제하기")
                    }
                }
            },
            content = { // content 파라미터에 값을 전달해야 합니다.
                Column {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        PostTitleText(bookItem.bookPostName)
                        MenuButton(
                            onClick = {
                                Log.d("MenuButton", "Clicked!")
                                coroutineScope.launch {
                                    sheetState.show()
                                }
                        })
                    }
                    Row {
                        bookItem.nickname?.let { text(text = it) }
                        Button(onClick = {
                            coroutineScope.launch {
                                openChatRoom(email, navController)
                            }
                        }) {
                            Text("챗톡")
                        }
                    }


                    Row {
                        bookItem.imageUrls.forEach { imageUrl ->
                            BookImage(imageUrl)
                        }
                    }

                    text(bookItem.bookName)
                    Row{
                        text(text = bookItem.bookPrice.toString())
                        Text(text = bookItem.bookRealPrice.toString(), color = Color.Gray)
                    }
                    Row{
                        text(text = "저자")
                        text(bookItem.author)
                    }
                    Row{
                        text(text = "출판사")
                        text(bookItem.publisher)
                    }
                    text(text = bookItem.bookComment)
                }
            }
        )
    }

    private suspend fun openChatRoom(email: String?, navController: NavController) {
        val token = TokenManager.getToken()
        Log.d("token", token.toString())
        Log.d("email", email.toString())
        if (email != null && token != null) {
            val emailBody = ChatRequest("sumin2@naver.com")
            val chatResponse = chatApi.getChatRooms(token, emailBody)
            if (chatResponse.isSuccessful) {
                Log.d("BookDetail", "getChatRooms : ${chatResponse.body()}")
                navController.navigate("personalChatTalk/${chatResponse.body()?.data?.chatRoomId}/${email}")
            } else {
                val errorBody = chatResponse.errorBody()?.string() // errorBody를 문자열로 변환
                Log.d("BookDetail", "getChatRooms : $errorBody")
            }
        }
    }


    @Composable
    private fun MenuButton(onClick: () -> Unit) = IconButton(
        onClick = onClick,
        modifier = Modifier
            .width(48.dp)
            .height(48.dp)
            .padding(end = 19.dp, start = 19.dp)
            .background(Color.Black)
        ) {
        Icon(
            painter = painterResource(id = R.drawable.ic_menu),
            tint = Color.Unspecified, // 로고 색 표현
            contentDescription = "메뉴 버튼",
            modifier = Modifier.clickable { onClick() }
        )
    }

    @Composable
    private fun text(text: String) = Text(
        text = text,
        fontSize = 30.sp
    )

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
}