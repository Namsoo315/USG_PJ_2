package com.example.myapplication.view.main

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.myapplication.R
import com.example.myapplication.view.component.IconButtonComponent.IconButton

object TopSectionComponent {
    @Composable
    fun view (navController: NavController) {
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 26.dp, start = 19.dp, end = 19.dp)
            ) {
            ChattingListButton(navController)
            AppNameText()
            MyPageButton()
        }
    }

    @Composable
    private fun ChattingListButton(navController: NavController) = Button(
        onClick = {
            Log.d("chattingListButton", "clicked")
            navController.navigate("chatList")
        },
        modifier = Modifier
            .padding(start = 10.dp, end=0.dp)
            .background(Color.Transparent)
    ) {
        Icon(
            painter = painterResource(id = R.drawable.ic_main_chatting_list),
            tint = Color.Unspecified, // 로고 색 표현
            contentDescription = "채팅 목록 버튼"
        )
    }

    @Composable
    private fun AppNameText() = Text(
        text = "책과 콩나무",
        fontSize = 30.sp
    )

    @Composable
    private fun MyPageButton() = IconButton(
        onClick = {},
        modifier = Modifier.padding(end = 19.dp)
    ) {
        Icon(
            painter = painterResource(id = R.drawable.ic_main_mypage),
            tint = Color.Unspecified, // 로고 색 표현
            contentDescription = "마이페이지 버튼"
        )
    }
}