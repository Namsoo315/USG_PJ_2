package com.example.myapplication

import ChatPageFragment
import ChatRoomPageFragment
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.core.app.ActivityCompat
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.myapplication.view.auth.LoginPageFragment
import com.example.myapplication.view.auth.JoinPageFragment
import com.example.myapplication.view.auth.TokenManager
import com.example.myapplication.view.register.RegisterInfoInputDetail
import com.example.myapplication.view.register.RegisterInfoInputForIsbn
import com.example.myapplication.view.register.RegisterInfoInputForManually
import com.example.myapplication.view.register.SelectInfoInputFragment
import android.provider.Settings
import android.Manifest
import android.util.Log
import com.example.myapplication.data.BookItem
import com.example.myapplication.data.BookUsingIsbn
import com.example.myapplication.view.detail.DetailPageFragment
import com.example.myapplication.view.main.MainPageFragment
import com.example.myapplication.view.modify.ModifyPageFragment


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // 권한 확인 및 요청 코드
        val permission = ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
        val permission2 = ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)

        if (permission != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                this,
                PERMISSIONS_STORAGE,
                REQUEST_EXTERNAL_STORAGE
            )
        }

        if (permission2 != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                this,
                PERMISSIONS_STORAGE,
                REQUEST_EXTERNAL_STORAGE
            )
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            if (!Environment.isExternalStorageManager()) {
                val intent = Intent(Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION)
                startActivity(intent)
            }
        }

        // setContent 내에서 @Composable 어노테이션 사용
        setContent {
            MainContent()
        }
    }

    companion object {
        private const val REQUEST_EXTERNAL_STORAGE = 1
        private val PERMISSIONS_STORAGE = arrayOf(
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
        )
    }
}


class RegisterViewModel() : ViewModel() {
    val bookItemIsbn = MutableLiveData<BookUsingIsbn>()
    var isbnCode: String = ""
}
class ChatTalkViewModel() : ViewModel() {
    var nickname: String = ""
}
class ModifyViewModel() : ViewModel() {
    var bookId: Int = 1
    val bookItem = MutableLiveData<BookItem>()
}

@Composable
fun MainContent() {
    val navController = rememberNavController()
    val context = LocalContext.current
    TokenManager.initialize(context)
    val token = remember { TokenManager.getToken() }
//    TokenManager.deleteToken()
    Log.d("token", token.toString())

    val registerViewModel: RegisterViewModel = viewModel()
    val chatTalkViewModel: ChatTalkViewModel = viewModel()
    val modifyViewModel: ModifyViewModel = viewModel()
    Surface(modifier = Modifier.fillMaxSize()) {
        NavHost(navController = navController, startDestination = if (token != null) "main" else "login") {
            composable("login") {
                LoginPageFragment.view(navController)
            }
            composable("join") {
                JoinPageFragment.view(navController)
            }
            composable("main") {
                MainPageFragment.view(navController)
//                DetailPageFragment.view(navController, modifyViewModel, detailViewModel)
            }
            composable("detail/{bookId}/{email}") { backStackEntry ->
                val arguments = backStackEntry.arguments
                val bookId = arguments?.getString("bookId")
                val email = arguments?.getString("email")
                if (bookId != null) {
                    DetailPageFragment.view(navController, modifyViewModel, bookId.toInt(), email)
                }
            }
            composable("selectInfoInput") {
                SelectInfoInputFragment.view(navController)
            }
            composable("registerInfoInputForIsbn") {
                RegisterInfoInputForIsbn.view(navController, registerViewModel)
            }
            composable("registerInfoInputForManually") {
                RegisterInfoInputForManually.view(navController, registerViewModel)
            }
            composable("modifyBookInfo") {
                ModifyPageFragment.view(modifyViewModel, navController)
            }
            composable("registerInfoInputDetail") {
                registerViewModel.bookItemIsbn.value?.let { bookItemIsbn ->
                    RegisterInfoInputDetail.view(registerViewModel.isbnCode, bookItemIsbn, navController)
                }
            }
            composable("personalChatTalk/{chatRoomId}/{email}") { backStackEntry ->
                val arguments = backStackEntry.arguments
                val chatRoomId = arguments?.getString("chatRoomId")
                val email = arguments?.getString("email")
                if (chatRoomId != null) {
                    ChatPageFragment.view(chatRoomId.toInt(), email)
                }
            }
            composable("chatList") {
                ChatRoomPageFragment.view(navController)
            }
        }
    }
}
