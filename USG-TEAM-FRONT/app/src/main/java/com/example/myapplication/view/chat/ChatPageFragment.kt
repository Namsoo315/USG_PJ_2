import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import com.example.myapplication.view.auth.TokenManager
import com.example.myapplication.view.chat.ChatService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.json.JSONObject
import ua.naiksoftware.stomp.Stomp
import ua.naiksoftware.stomp.StompClient
import ua.naiksoftware.stomp.dto.StompCommand
import ua.naiksoftware.stomp.dto.StompHeader
import ua.naiksoftware.stomp.dto.StompMessage
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ChatPageFragment {

    private var stompClient: StompClient? = null
    private val receivedMessages = mutableStateListOf<String>() // List of messages received from the server

    private val retrofit = Retrofit.Builder()
        .baseUrl("http://34.64.152.213:8081")
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val chatService = retrofit.create(ChatService::class.java)

    @Composable
    fun view(chatRoomId: Int, email: String?) {
        Log.d("chatRoomId", chatRoomId.toString())
        if (email != null) {
            Log.d("email", email)
        }
        LaunchedEffect(Unit) {
            // 페이지에 처음 진입할 때 소켓 연결
            if (email != null) {
                fetchMessages(email)
            }
            connectToWebSocket(chatRoomId)
            //처음 진입할 때 한 번만 메시지 요청
        }

        DisposableEffect(Unit) {
            // 페이지를 이탈할 때 소켓 연결 해제
            onDispose {
                disconnectWebSocket()
            }
        }
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            if (email != null) {
                SendMessage(email, chatRoomId)
            }
            Spacer(modifier = Modifier.height(16.dp))
            ReceivedMessages()
        }
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun SendMessage(email: String, chatRoomId: Int) {
        val context = LocalContext.current
        val (message, setMessage) = remember { mutableStateOf("") }

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            TextField(
                value = message,
                onValueChange = setMessage,
                label = { Text("Enter message") },
                keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Send),
                keyboardActions = KeyboardActions(onSend = { sendMessage(context, message, email, chatRoomId) })
            )
            Button(onClick = { sendMessage(context, message, email, chatRoomId) }) {
                Text("Send")
            }
        }
    }

    @Composable
    fun ReceivedMessages() {
        Column {
            receivedMessages.forEach { message ->
                Text(text = message, modifier = Modifier.padding(end = 10.dp))
            }
        }
    }

    private fun connectToWebSocket(chatRoomId: Int) {
        val serverUrl = "ws://34.64.152.213:8081/ws"
        stompClient = Stomp.over(Stomp.ConnectionProvider.OKHTTP, serverUrl)
        stompClient?.connect()

        stompClient?.lifecycle()?.subscribe { lifecycleEvent ->
            when (lifecycleEvent.type) {
                ua.naiksoftware.stomp.dto.LifecycleEvent.Type.OPENED -> {
                    subscribeToChatTopic(chatRoomId)
                }
                ua.naiksoftware.stomp.dto.LifecycleEvent.Type.CLOSED -> {
                    // 연결이 닫힐 때 소켓 연결을 해제합니다.
                    disconnectWebSocket()
                    Log.e("연결 끊김","메시지 연결")
                }
                else -> {
                    // 다른 이벤트에 대한 처리는 필요에 따라 추가할 수 있습니다.
                }
            }
        }
    }

    private fun disconnectWebSocket() {
        stompClient?.disconnect()
        stompClient = null
    }


    private fun subscribeToChatTopic(chatRoomId: Int) {
        val chatRoomId = chatRoomId
        val destination = "/topic/chat/$chatRoomId"

        stompClient?.topic(destination)?.subscribe() { topicMessage ->
            val message = topicMessage.payload // Received message
            handleReceivedMessage(message)
        }
    }

    private fun handleReceivedMessage(receivedMessage: String) {
        val jsonObject = JSONObject(receivedMessage)
        val body = jsonObject.optJSONObject("body") // "body" 키로부터 JSON 객체 추출

        if (body != null) {
            processMessageData(body)
        } else {
            println("Received message does not contain 'body' object.")
        }
    }

    private fun processMessageData(body: JSONObject) {
        val data = body.optJSONObject("data") // "data" 키로부터 JSON 객체 추출

        if (data != null) {
            val senderEmail = data.optString("senderEmail", "")
            val message = data.optString("message", "")
            val timestamp = data.optString("timestamp", "")

            receivedMessages.add("Sender: $senderEmail - Message: $message - Timestamp: $timestamp")
        } else {
            println("Body does not contain 'data' object.")
        }
    }


    private fun sendMessage(context: android.content.Context, message: String, email: String, chatRoomId: Int) {
        val receiverEmail = email
        val chatRoomId = chatRoomId

        val jsonObject = JSONObject().apply {
            put("message", message)
            put("receiverEmail", receiverEmail)
            put("chatRoomId", chatRoomId)
            put("timestamp","")
        }

        val token = TokenManager.getToken() ?: ""
        val headers = mutableListOf<StompHeader>().apply {
            add(StompHeader("destination", "/app/chat/$chatRoomId"))
            add(StompHeader("Authorization", "Bearer $token"))
        }

        val stompMessage = StompMessage(
            StompCommand.SEND,
            headers,
            jsonObject.toString()
        )

        stompClient?.send(stompMessage)?.subscribe()
    }

    private fun fetchMessages(email: String) {
        val receiverEmail = email
        val token = TokenManager.getToken() ?: ""

        GlobalScope.launch(Dispatchers.IO) {
            try {
                val response = chatService.getMessages(token, receiverEmail)
                val messages = response.data
                messages.forEach { message ->
                    val formattedMessage = "Sender: ${message.senderId} Receiver: ${message.receiverId}- Message: ${message.message} - Timestamp: ${message.timestamp}"
                    receivedMessages.add(formattedMessage)
                }
            } catch (e: Exception) {
                Log.e("ChatPageFragment", "Error fetching messages: ${e.message}")
            }
        }
    }
}

