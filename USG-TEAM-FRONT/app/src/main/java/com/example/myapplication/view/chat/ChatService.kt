package com.example.myapplication.view.chat

import com.google.gson.annotations.SerializedName
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Query

interface ChatService {
    @GET("/api/messages")
    suspend fun getMessages(
        @Header("Authorization") token: String,
        @Query("receiverEmail") receiverEmail: String
    ): MessagesResponse // MessagesResponse에 대한 적절한 모델 클래스가 필요합니다.

    @GET("/api/chat-rooms/")
    suspend fun getChatRooms(@Header("Authorization") token: String): List<ChatRoom>

    @POST("/api/chat-rooms")
    suspend fun getChatRooms(
        @Header("Authorization") token: String,
        @Body chatRequest: ChatRequest
    ): Response<ChatResponse>


}

data class ChatRequest(
    val opponentEmail: String
)

data class ChatResponse(
    val data: ChatData,
    val message: String
)

data class ChatData(
    val chatRoomId: Int
)

data class Message(
    val message: String,
    @SerializedName("senderEmail") val senderId: String,
    @SerializedName("receiverEmail") val receiverId: String,
    val timestamp: String,
    @SerializedName("chatRoomId") val chatRoomId: Long
)

data class MessagesResponse(
    val data: List<Message>,
    val message: String?
)

data class ChatRoom(
    val chatRoomId: Long,
    val opponentEmail: String,
    val opponentNickName: String
    // 필요한 다른 속성들을 여기에 추가할 수 있습니다.
)