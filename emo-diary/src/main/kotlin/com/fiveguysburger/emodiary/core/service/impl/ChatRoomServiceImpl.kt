package com.fiveguysburger.emodiary.core.service.impl

import com.fasterxml.jackson.databind.ObjectMapper
import com.fiveguysburger.emodiary.core.service.ChatRoomService
import com.fiveguysburger.emodiary.mcp.client.tool.DirectToolCall
import org.springframework.ai.chat.client.ChatClient
import org.springframework.ai.chat.memory.ChatMemory
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.Instant

@Service
class ChatRoomServiceImpl(
    chatClientBuilder: ChatClient.Builder,
    private val directToolCall: DirectToolCall,
    private val objectMapper: ObjectMapper
) : ChatRoomService {

    private val chatClient: ChatClient = chatClientBuilder.build()

    @Transactional
    override fun createChatRoom(userId: String): String {
        require(userId.isNotBlank()) { "User ID cannot be null or blank." }
        val today = Instant.now()

        val document =
            mapOf(
                "userId" to userId,
                "date" to today,
            )
        return directToolCall.insertDocument("daily_chats", document)
    }

    @Transactional
    override fun sendMessage(
        chatroomId: String,
        userId: String,
        message: String,
    ): String {
        require(chatroomId.isNotBlank()) { "Chatroom ID cannot be null or blank." }
        require(userId.isNotBlank()) { "User ID cannot be null or blank." }
        require(message.isNotBlank()) { "message cannot be null or blank." }
        val document =
            mapOf(
                "dailyChatId" to chatroomId,
                "sender" to "user",
                "text" to message,
                "timestamp" to Instant.now(),
            )

        directToolCall.insertDocument("messages", document)

        val chatResponse =
            chatClient.prompt()
                .user(message)
                .advisors { a -> a.param(ChatMemory.CONVERSATION_ID, chatroomId) }
                .call()

        val llmResponse = chatResponse.content().toString()

        val llmDocument =
            mapOf(
                "dailyChatId" to chatroomId,
                "sender" to "llm",
                "text" to llmResponse,
                "timestamp" to Instant.now(),
            )
        directToolCall.insertDocument("messages", llmDocument, null)

        return llmResponse
    }

    override fun getAllMessages(chatroomId: String, userId: String): String {
        require(chatroomId.isNotBlank()) { "Chatroom ID cannot be null or blank." }
        require(userId.isNotBlank()) { "User ID cannot be null or blank." }

        val filter = mapOf("dailyChatId" to chatroomId)
        val sort = mapOf("createdAt" to 1)

        return directToolCall.findDocuments(
            collection = "messages",
            filter = filter,
            limit = 100, // 한 번에 가져올 메시지 수를 지정합니다. 기본값(10)보다 넉넉하게 설정하는 것이 좋습니다.
            null,
            sort = sort
        )
    }
}
