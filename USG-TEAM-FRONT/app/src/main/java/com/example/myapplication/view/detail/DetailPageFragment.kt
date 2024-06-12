package com.example.myapplication.view.detail

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.myapplication.view.component.TopNavigationComponent
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import com.example.myapplication.ModifyViewModel


object DetailPageFragment {

    @Composable
    fun view(
        navController: NavHostController,
        modifyViewModel: ModifyViewModel,
        bookId: Int,
        email: String?
    ) = Column(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight()

    ) {
        TopNavigationComponent.view()
        BookDetailScreen(bookId, email, modifyViewModel, navController)
    }

    @Composable
    fun BookDetailScreen(bookId: Int, email: String?, modifyViewModel: ModifyViewModel, navController: NavController) {
        BookDetailComponent.view(bookId, email, modifyViewModel, navController)
    }
}