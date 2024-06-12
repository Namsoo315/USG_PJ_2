import android.app.Activity
import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.myapplication.view.auth.TokenManager
import com.example.myapplication.view.chat.ChatRoom
import com.google.gson.Gson
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import java.io.IOException
import java.util.concurrent.TimeUnit

private const val API_BASE_URL = "http://34.64.152.213:8081"

object ChatRoomPageFragment {
    private var chatRooms by mutableStateOf<List<ChatRoom>>(emptyList())
    @Composable
    fun view(navController: NavHostController) {
        ConnectScreen(navController)
    }
    @Composable
    fun ConnectScreen(navController: NavHostController) {
        val context = LocalContext.current
        val (connected, setConnected) = remember { mutableStateOf(false) }

        // 채팅방 목록 조회
        fetchChatRooms(context)

        // 채팅방 목록 출력
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            if (!connected) {
                // 연결이 되어있지 않은 경우에만 연결 버튼을 표시
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Button(onClick = { connectToSTOMP(context, setConnected) }) {
                        Text("Connect")
                    }
                    // 토큰 삭제 버튼
                    Button(onClick = { deleteTokenAndDisconnect(context, setConnected) }) {
                        Text("Delete Token")
                    }
                }
                Spacer(modifier = Modifier.height(16.dp))
            } else {
//                ChatPageFragment.view(chatRoomId, email)
            }

            // 채팅방 목록 출력
            Column(
                modifier = Modifier.padding(vertical = 16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                chatRooms.forEach { chatRoom ->
                    Button(onClick = { enterPersonalChat(chatRoom.chatRoomId.toInt(), chatRoom.opponentEmail, navController) }) {
                        Text(text = "Chat Room ID: ${chatRoom.chatRoomId}, Opponent: ${chatRoom.opponentEmail}, Nickname: ${chatRoom.opponentNickName}")
                    }
                }
            }
        }
    }

    private fun enterPersonalChat(chatRoomId: Int, email: String, navController: NavHostController) {
        navController.navigate("personalChatTalk/${chatRoomId}/${email}")
    }

    private fun connectToSTOMP(context: Context, setConnected: (Boolean) -> Unit) {
        try {
            // 토큰 가져오기
            val token = TokenManager.getToken() ?: ""

            // ChatRoom 생성 요청 API 호출
            val requestBody = ChatRoomRequest(opponentEmail = "namsoo2@email") // 수정 필요
            val createRequest = Request.Builder()
                .url(API_BASE_URL + "/api/chat-rooms")
                .addHeader("Authorization", "Bearer $token") // 헤더에 토큰 추가
                .post(RequestBody.create("application/json".toMediaTypeOrNull(), Gson().toJson(requestBody)))
                .build()

            // OkHttpClient 생성
            val client = OkHttpClient.Builder()
                .readTimeout(0, TimeUnit.MILLISECONDS)
                .build()

            // ChatRoom 생성 및 ID 조회 API 호출
            client.newCall(createRequest).enqueue(object : Callback {
                override fun onFailure(call: Call, e: IOException) {
                    // 실패 시 처리
                    Log.e("WebSocket", "ChatRoom 생성 실패: ${e.message}")
                    (context as? Activity)?.runOnUiThread {
                        Toast.makeText(context, "채팅방 생성에 실패했습니다.", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onResponse(call: Call, response: Response) {
                    // 응답 처리
                    if (response.isSuccessful) {
                        // ChatRoom 생성 및 ID 조회 성공 시 연결 상태를 설정
                        setConnected(true)
                    } else {
                        Log.e("WebSocket", "ChatRoom 생성 실패: ${response.code}")
                    }
                }
            })
        } catch (e: Exception) {
            // 예외 처리
            Log.e("WebSocket", "WebSocket 연결 실패: ${e.message}")
            (context as? Activity)?.runOnUiThread {
                Toast.makeText(context, "연결에 실패했습니다. 다시 시도해주세요2.", Toast.LENGTH_SHORT).show()
            }
        }
    }


    // 토큰 삭제 및 연결 해제
    private fun deleteTokenAndDisconnect(context: Context, setConnected: (Boolean) -> Unit) {
        // 토큰 삭제
        TokenManager.deleteToken()
        // 연결 해제
        setConnected(false)
        // 연결 상태에 따라 메시지 출력
        Toast.makeText(context, "토큰이 삭제되었습니다.", Toast.LENGTH_SHORT).show()
    }

    data class ChatRoomRequest(
        val opponentEmail: String
    )
    private fun fetchChatRooms(context: Context) {
        try {
            // 토큰 가져오기
            val token = TokenManager.getToken() ?: ""

            // ChatRoom 목록 조회 API 호출
            val request = Request.Builder()
                .url(API_BASE_URL + "/api/chat-rooms/")
                .addHeader("Authorization", "Bearer $token") // 헤더에 토큰 추가
                .get()
                .build()

            // OkHttpClient 생성
            val client = OkHttpClient.Builder()
                .readTimeout(0, TimeUnit.MILLISECONDS)
                .build()

            // ChatRoom 목록 조회 API 호출
            client.newCall(request).enqueue(object : Callback {
                override fun onFailure(call: Call, e: IOException) {
                    // 실패 시 처리
                    Log.e("ChatRoomPageFragment", "Failed to fetch chat rooms: ${e.message}")
                    (context as? Activity)?.runOnUiThread {
                        Toast.makeText(context, "채팅방 목록을 가져오는 데 실패했습니다.", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onResponse(call: Call, response: Response) {
                    // 응답 처리
                    if (response.isSuccessful) {
                        val responseBody = response.body?.string()
                        val responseData = Gson().fromJson(responseBody, GetChatRoomsResponse::class.java)
                        // ChatRoom 목록 업데이트
                        updateChatRooms(responseData.data)
                    } else {
                        Log.e("ChatRoomPageFragment", "Failed to fetch chat rooms: ${response.code}")
                    }
                }
            })
        } catch (e: Exception) {
            // 예외 처리
            Log.e("ChatRoomPageFragment", "Failed to fetch chat rooms: ${e.message}")
            (context as? Activity)?.runOnUiThread {
                Toast.makeText(context, "채팅방 목록을 가져오는 데 실패했습니다.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun updateChatRooms(newChatRooms: List<ChatRoom>) {
        chatRooms = newChatRooms
    }

    data class GetChatRoomsResponse(
        val data: List<ChatRoom>,
        val message: String
    )
}
