package com.example.myapplication.view.register

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.myapplication.view.main.BookListComponent
import com.example.myapplication.view.main.SellBookButtonComponent
import com.example.myapplication.view.main.TopSectionComponent

object SelectInfoInputFragment {
    @Composable
    fun view(navController: NavController) {
        Column {
            Text("책 정보 등록")
            EnterIsbn(navController)
            EnterManually(navController)
        }

    }

    @Composable
    private fun EnterIsbn(navController: NavController) = Button(onClick = { navController.navigate("registerInfoInputForIsbn") }) {
        Text(text = "ISBN 입력")
    }

    @Composable
    private fun EnterManually(navController: NavController) = Button(onClick = { navController.navigate("registerInfoInputForManually") }) {
        Text(text = "수동으로 입력")
    }
}