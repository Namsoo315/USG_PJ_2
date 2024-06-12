package com.example.myapplication.view.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.myapplication.R
import com.example.myapplication.view.component.IconButtonComponent.IconButton

object TopNavigationComponent {
    @Composable
    fun view() = Row(
        modifier = Modifier
            .height(40.dp)
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        GoToBackButton()
        GoToMainButton()
    }

    @Composable
    private fun GoToBackButton() = IconButton(
        onClick = {},
        modifier = Modifier.padding(end = 19.dp)
    ) {
        Icon(
            painter = painterResource(id = R.drawable.ic_top_nav_back),
            tint = Color.Unspecified, // 로고 색 표현
            contentDescription = "뒤로가기 버튼"
        )
    }

    @Composable
    private fun GoToMainButton() = IconButton(
        onClick = {},
        modifier = Modifier.padding(end = 19.dp)
    ) {
        Icon(
            painter = painterResource(id = R.drawable.ic_top_nav_main),
            tint = Color.Unspecified, // 로고 색 표현
            contentDescription = "홈으로 이동 버튼"
        )
    }
}