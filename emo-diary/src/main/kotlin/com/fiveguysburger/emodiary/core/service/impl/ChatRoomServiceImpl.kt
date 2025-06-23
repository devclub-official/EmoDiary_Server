package com.fiveguysburger.emodiary.core.service.impl

import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.ObjectMapper
import com.fiveguysburger.emodiary.core.dto.*
import com.fiveguysburger.emodiary.core.service.ChatRoomService
import com.fiveguysburger.emodiary.mcp.client.tool.DirectToolCall
import org.springframework.ai.chat.client.ChatClient
import org.springframework.ai.chat.memory.ChatMemory
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.Instant

@Service
class ChatRoomServiceImpl(
    private val directToolCall: DirectToolCall,
    private val objectMapper: ObjectMapper
) : ChatRoomService {
  
    @Transactional
    override fun createChatRoom(userId: String): ApiResponse<CreateDiaryRoomResponse> {
        require(userId.isNotBlank()) { "User ID cannot be null or blank." }
 
        val today = Instant.now()
        val document =
            mapOf(
                "userId" to userId,
                "date" to today,
            )

        val rawResultString = directToolCall.insertDocument("daily_chats", document)

        val regex = "- ID: ([a-f0-9]+)".toRegex()
        val matchResult = regex.find(rawResultString)

        return if (matchResult != null) {
            val insertedId = matchResult.groupValues[1]
            val responseData = CreateDiaryRoomResponse(dailyChatId = insertedId)
            ApiResponse.success(
                data = responseData,
                message = "채팅방이 성공적으로 생성되었습니다."
            )
        } else {
            ApiResponse.error(message = "채팅방 생성 후 ID를 파싱하는데 실패했습니다.")
        }
   
        return directToolCall.insertDocument("daily_chats", document)
    }

    @Transactional
    override fun sendMessage(
        chatroomId: String,
        userId: String,
        message: String,
    ): ApiResponse<ChatMessageResponse> {
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
                .system("사용자는 일기를 작성 중이야. 너는 그것을 도와주는 도우미야. 사용자의 말에 공감해주고, 더 많이 하루를 돌이켜볼 수 있도록 말을 이끌어내봐.")
                .user(message)
                .advisors { a -> a.param(ChatMemory.CONVERSATION_ID, chatroomId) }
                .call()

        val llmResponseText = chatResponse.content().toString()

        val llmDocument =
            mapOf(
                "dailyChatId" to chatroomId,
                "sender" to "llm",
                "text" to llmResponseText,
                "timestamp" to Instant.now(),
            )
        directToolCall.insertDocument("messages", llmDocument, null)

        return ApiResponse.success(
            data = ChatMessageResponse(text = llmResponseText),
            message = "메세지가 성공적으로 전송되었습니다"
        )
    }

    override fun getAllMessages(chatroomId: String, userId: String): ApiResponse<Messages> {
        require(chatroomId.isNotBlank()) { "Chatroom ID cannot be null or blank." }
        require(userId.isNotBlank()) { "User ID cannot be null or blank." }

        val filter = mapOf("dailyChatId" to chatroomId)
        val sort = mapOf("timestamp" to 1)
        val projection = mapOf(
            "_id" to 1,
            "dailyChatId" to 1,
            "sender" to 1,
            "text" to 1,
            "timestamp" to 1
        )

        val dirtyJsonString: String = directToolCall.findDocuments(
            collection = "messages",
            filter = filter,
            limit = 100,
            projection = projection,
            sort = sort
        )

        val outerList: List<Map<String, String>> = objectMapper.readValue(
            dirtyJsonString,
            object : TypeReference<List<Map<String, String>>>() {}
        )

        if (outerList.isEmpty() || !outerList[0].containsKey("text")) {
            return ApiResponse.error(
                message = "메세지를 읽어오는 데에 실패하였습니다."
            )
        }
        var contentString = outerList[0]["text"]!!


        contentString = contentString.substringAfter("documents:\n")
        contentString = contentString.replace(Regex("\"ObjectId\\(\\\\\"(.+?)\\\\\"\\)\""), "\"$1\"")
        contentString = contentString.split("\n\n").joinToString(separator = ",", prefix = "[", postfix = "]")

        val diaryMessagesDtoList: List<DiaryMessages> = objectMapper.readValue(
            contentString,
            object : TypeReference<List<DiaryMessages>>() {}
        )

        return ApiResponse.success(
            data = Messages(messages = diaryMessagesDtoList),
            message = "메세지를 모두 읽어오는 데 성공하였습니다."
        )
    }

    override fun requestDiaryAnalysis(chatroomId: String, userId: String): ApiResponse<AnalysisResponse> {
        require(chatroomId.isNotBlank()) { "Chatroom ID cannot be null or blank." }
        require(userId.isNotBlank()) { "User ID cannot be null or blank." }

        val documentsJson = this.getAllMessages(chatroomId, userId)

        val chatResponse = chatClient.prompt()
            .system("너는 EmoDiary의 다정한 친구 '감정이'야. 대화 내용을 분석하고 사용자의 감정을 짚어주는 공감 능력이 뛰어난 일기 분석 전문가야. 시간의 흐름에 따라 감정이 어떻게 변화했는지 구체적인 예시를 들어 설명해줘. 그리고 사용자의 말에 깊이 공감하는 문장을 포함해줘. 마지막으로, 따뜻한 응원과 덕담을 덧붙여줘.")
            .user(documentsJson.toString())
            .call()
            .content()

        return ApiResponse.success(
            data = AnalysisResponse(analysis = chatResponse),
            message = "일기 분석에 성공하였습니다."
        )

        return llmResponse
                "userId" to userId.toInt(),
                "date" to today,
            )
            
        return directToolCall.insertDocument("chatrooms", document)
    }
}
