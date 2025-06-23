package com.fiveguysburger.emodiary.core.controller

import com.fiveguysburger.emodiary.core.dto.*
import com.fiveguysburger.emodiary.core.service.ChatRoomService
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.ResponseEntity
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody

import com.fiveguysburger.emodiary.core.service.ChatRoomService
import org.springframework.http.ResponseEntity
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.PostMapping

@RestController()
@RequestMapping("/api/v1")
@Tag(name = "일기", description = "일기 API")
class DiaryController(
    private val chatRoomService: ChatRoomService,
) {
    @PostMapping("/chatrooms")
    fun createChatRoom(
        @AuthenticationPrincipal userId: String,
    ): ResponseEntity<ApiResponse<CreateDiaryRoomResponse>> {
        val result = chatRoomService.createChatRoom(userId)
        return ResponseEntity.ok(result)
    }

    @PostMapping("/chatrooms/{chatroomId}")
    fun sendChatMessage(
        @AuthenticationPrincipal userId: String,
        @PathVariable("chatroomId") chatroomId: String,
        @RequestBody request: ChatMessageRequest,
    ): ResponseEntity<ApiResponse<ChatMessageResponse>> {
        val llmResponse =
            chatRoomService.sendMessage(
                chatroomId = chatroomId,
                userId = userId,
                message = request.message,
            )

        return ResponseEntity.ok(llmResponse)
    }

    @GetMapping("/chatrooms/{chatroomId}")
    fun getChatRoomMessages(
        @AuthenticationPrincipal userId: String,
        @PathVariable("chatroomId") chatroomId: String,
    ) : ResponseEntity<ApiResponse<Messages>>{
        return ResponseEntity.ok(
            chatRoomService.getAllMessages(
                chatroomId = chatroomId,
                userId = userId
            )
        )
    }

    @GetMapping("/chatrooms/{chatroomId}/analysis")
    fun analysisDiary(
        @AuthenticationPrincipal userId: String,
        @PathVariable("chatroomId") chatroomId: String,
    ) : ResponseEntity<ApiResponse<AnalysisResponse>> {
        return ResponseEntity.ok(
            chatRoomService.requestDiaryAnalysis(
                chatroomId = chatroomId,
                userId = userId
            )
        )
    }
}
