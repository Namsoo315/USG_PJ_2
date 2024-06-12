package com.example.myapplication.view.auth

import android.content.Context
import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.myapplication.api.ApiService
import com.example.myapplication.api.LoginRequest
import com.example.myapplication.api.LoginResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object LoginPageFragment {

    @OptIn(ExperimentalMaterial3Api::class, ExperimentalComposeUiApi::class)
    @Composable
    fun view(navController: NavController) {
        val context = LocalContext.current
        val keyboardController = LocalSoftwareKeyboardController.current
        // TokenManager를 초기화합니다.
        TokenManager.initialize(context)

        // 입력된 이메일과 비밀번호를 저장할 변수
        var email by remember { mutableStateOf("") }
        var password by remember { mutableStateOf("") }

        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = "책과 콩나무는 로그인이 필요한 서비스입니다.",
                modifier = Modifier.padding(bottom = 8.dp)
            )

            Text(
                text = "로그인",
                modifier = Modifier.padding(bottom = 8.dp)
            )

            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = { Text("이메일") },
                keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Next),
                keyboardActions = KeyboardActions(
                    onNext = { keyboardController?.hide() }
                ),
                modifier = Modifier.padding(bottom = 8.dp)
            )

            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                label = { Text("비밀번호") },
                visualTransformation = PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Done),
                keyboardActions = KeyboardActions(
                    onDone = { keyboardController?.hide() }
                ),
                modifier = Modifier.padding(bottom = 8.dp),
            )

            Button(
                onClick = {
                    // 로그인 함수 호출
                    login(email, password, context, navController)
                },
                modifier = Modifier.padding(top = 16.dp)
            ) {
                Text(text = "로그인")
            }


            Text(
                text = "회원이 아니라면?",
                modifier = Modifier.padding(bottom = 8.dp)
            )

            // 회원가입 버튼 클릭 시 JoinPageFragment로 이동
            Button(
                onClick = {
                    navController.navigate("join") // 또는 다른 방법을 사용하여 목적지로 이동
                },
                modifier = Modifier.padding(bottom = 8.dp)
            ) {
                Text(text = "회원가입")
            }
        }
    }

    private fun initializeApiService(context: Context): ApiService {
        val retrofit = Retrofit.Builder()
            .baseUrl("http://34.64.152.213:9090/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        return retrofit.create(ApiService::class.java)
    }

    private fun login(email: String, password: String, context: Context, navController: NavController) {
        val apiService = initializeApiService(context)
        Log.d("log", "login api start")
        val loginRequest = LoginRequest(email, password)

        apiService.login(loginRequest).enqueue(object : Callback<LoginResponse> {
            override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                if (response.isSuccessful) {
                    val loginResponse = response.body()
                    val token = loginResponse?.token
                    if (token != null) {
                        // 로그인 성공 처리 및 토큰 저장
                        TokenManager.saveToken(token)
                        Log.d("log", "login success")
                        Log.d("log", token)

                        navController.navigate("main") // "main"은 MainFragment의 목적지 ID입니다.
                    }
                } else {
                    // 로그인 실패 처리
                    Log.d("log", "login failed")
                }
            }

            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                // 네트워크 오류 처리
                Log.d("log", "login failed, network error", t)
            }
        })
    }
}
