package com.example.myapplication.view.main

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController

object SellBookButtonComponent {
    @Composable
    fun SellBookButton(modifier: Modifier, navController: NavController) = Button(
        onClick = { navController.navigate("selectInfoInput") },
        modifier = modifier,
        colors = ButtonDefaults.buttonColors(
            containerColor = Color.Gray
        )
    ) {
        Text(
            text = "책 판매",
            fontSize = 25.sp
        )
    }
}