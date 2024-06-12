package com.example.myapplication.view.auth

import android.app.AlertDialog
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
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.navigation.ActivityNavigator
import androidx.navigation.NavController
import com.example.myapplication.api.ApiService
import com.example.myapplication.api.EmailCheckRequest
import com.example.myapplication.api.EmailCheckResponse
import com.example.myapplication.api.JoinRequest
import com.example.myapplication.api.JoinResponse
import com.example.myapplication.api.NicknameCheckRequest
import com.example.myapplication.api.NicknameCheckResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object JoinPageFragment{
    @OptIn(ExperimentalMaterial3Api::class, ExperimentalComposeUiApi::class)
    @Composable
    fun view(navController: NavController) {
        val context = LocalContext.current
        val keyboardController = LocalSoftwareKeyboardController.current

        // 상태 변수 선언
        var password by remember { mutableStateOf("") }
        var confirmPassword by remember { mutableStateOf("") }
        var passwordsMatch by remember { mutableStateOf(true) } // 비밀번호 일치 여부
        var email by remember {
            mutableStateOf("")
        }
        var nickname by remember {
            mutableStateOf("")
        }
        var isEmailChecked by remember { mutableStateOf(false) }
        var isNicknameChecked by remember { mutableStateOf(false) }


        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = "회원가입",
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

            Button(
                onClick = {
                    checkEmail(email = email,
                        onSuccess = { isEmailChecked = true },
                        onFailure = { /* 이메일 중복일 때 처리 */ },
                        context
                    )
                },
                modifier = Modifier.padding(top = 16.dp)
            ) {
                Text(text = "이메일 중복 확인")
            }

            OutlinedTextField(
                value = nickname,
                onValueChange = { nickname = it },
                label = { Text("별명") },
                keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Next, keyboardType = KeyboardType.Text),
                keyboardActions = KeyboardActions(
                    onNext = { keyboardController?.hide() }
                ),
                modifier = Modifier.padding(bottom = 8.dp)
            )

            Button(
                onClick = {
                    checkNickname(nickname = nickname,
                        onSuccess = { isNicknameChecked = true },
                        onFailure = { /* 별명 중복일 때 처리 */ },
                        context)
                },
                modifier = Modifier.padding(top = 16.dp)
            ) {
                Text(text = "별명 중복 확인")
            }


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

            OutlinedTextField(
                value = confirmPassword,
                onValueChange = { confirmPassword = it },
                label = { Text("비밀번호 확인") },
                visualTransformation = PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Done),
                keyboardActions = KeyboardActions(
                    onDone = { keyboardController?.hide() }
                ),
                modifier = Modifier.padding(bottom = 8.dp),
                isError = !passwordsMatch // 에러 표시 여부
            )

            // 비밀번호 확인
            if (password != confirmPassword) {
                passwordsMatch = false
            } else {
                passwordsMatch = true
            }

            Button(
                onClick = {
                    // 회원가입 버튼 클릭 시
                    if (isEmailChecked && isNicknameChecked && passwordsMatch) {
                        join(email, nickname, password, context, navController)
                    }
                },
                enabled = isEmailChecked && isNicknameChecked && passwordsMatch, // 버튼 활성화 여부
                modifier = Modifier.padding(top = 16.dp)
            ) {
                Text(text = "회원가입")
            }

            Text(
                text = "회원이라면?",
                modifier = Modifier.padding(bottom = 8.dp)
            )

            // 로그인 버튼 클릭 시 LoginPageFragment로 이동
            Button(
                onClick = {
                    navController.navigate("login") // 또는 다른 방법을 사용하여 목적지로 이동
                },
                modifier = Modifier.padding(bottom = 8.dp)
            ) {
                Text(text = "로그인")
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

    private fun join(email: String, nickname: String, password: String, context: Context, navController: NavController) {
        val apiService = initializeApiService(context)
        Log.d("log", "join api start")
        val joinRequest = JoinRequest(email, nickname, password)

        apiService.join(joinRequest).enqueue(object : Callback<JoinResponse> {
            override fun onResponse(call: Call<JoinResponse>, response: Response<JoinResponse>) {
                if (response.isSuccessful) {
                    // 회원가입 성공 처리
                    Log.d("log", "join success")
                    // 여기에 회원가입 성공 시 필요한 로직을 추가하세요
                    navController.navigate("login") // 또는 다른 방법을 사용하여 목적지로 이동
                } else {
                    // 회원가입 실패 처리
                    Log.d("log", "join failed")
                }
            }

            override fun onFailure(call: Call<JoinResponse>, t: Throwable) {
                // 네트워크 오류 처리
                Log.d("log", "join failed, network error", t)
            }
        })
    }

    private fun checkEmail(email: String, onSuccess: () -> Unit, onFailure: () -> Unit, context: Context) {
        val apiService = initializeApiService(context)
        val emailRequest = EmailCheckRequest(email)

        apiService.checkEmail(emailRequest).enqueue(object : Callback<EmailCheckResponse> {
            override fun onResponse(call: Call<EmailCheckResponse>, response: Response<EmailCheckResponse>) {
                if (response.isSuccessful) {
                    val emailCheckResponse = response.body()
                    val isEmailAvailable = emailCheckResponse?.isAvailable ?: false
                    if (isEmailAvailable) {
                        onSuccess() // 이메일 사용 가능할 때 처리
                        Log.d("가능 여부", "가능")
                        showAlert(context, "이메일 중복 여부 알림", "해당 이메일 사용 가능 합니다!")
                    } else {
                        onFailure() // 이메일이 이미 사용 중일 때 처리
                        Log.d("가능 여부", "불가능")
                        showAlert(context, "이메일 중복 여부 알림", "해당 이메일 사용 불가 합니다!")
                    }
                } else {
                    onFailure() // 서버 오류 등으로 확인 실패 시 처리
                }
            }

            override fun onFailure(call: Call<EmailCheckResponse>, t: Throwable) {
                onFailure() // 네트워크 오류 등으로 확인 실패 시 처리
                showAlert(context, "서버 오류 알림", "죄송합니다! 잠시 후 다시 시도해주세요.")
            }
        })
    }

    private fun checkNickname(nickname: String, onSuccess: () -> Unit, onFailure: () -> Unit, context: Context) {
        val apiService = initializeApiService(context)
        val nicknameRequest = NicknameCheckRequest(nickname)

        apiService.checkNickname(nicknameRequest).enqueue(object : Callback<NicknameCheckResponse> {
            override fun onResponse(call: Call<NicknameCheckResponse>, response: Response<NicknameCheckResponse>) {
                if (response.isSuccessful) {
                    val nicknameCheckResponse = response.body()
                    val isNicknameAvailable = nicknameCheckResponse?.isAvailable ?: false
                    if (isNicknameAvailable) {
                        onSuccess() // 별명 사용 가능할 때 처리
                        showAlert(context, "닉네임 중복 여부 알림", "해당 닉네임 사용 가능 합니다!")
                    } else {
                        onFailure() // 별명이 이미 사용 중일 때 처리
                        showAlert(context, "닉네임 중복 여부 알림", "해당 닉네임 사용 불가 합니다!")
                    }
                } else {
                    onFailure() // 서버 오류 등으로 확인 실패 시 처리
                }
            }

            override fun onFailure(call: Call<NicknameCheckResponse>, t: Throwable) {
                onFailure() // 네트워크 오류 등으로 확인 실패 시 처리
                showAlert(context, "서버 오류 알림", "죄송합니다! 잠시 후 다시 시도해주세요.")
            }
        })
    }

    // 중복 사용 알림창을 생성하는 함수
    private fun showAlert(context: Context, title: String, message: String) {
        val builder = AlertDialog.Builder(context)
        builder.setTitle(title)
        builder.setMessage(message)
        builder.setPositiveButton("확인", null)
        val dialog = builder.create()
        dialog.show()
    }
}
