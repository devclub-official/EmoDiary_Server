package com.fiveguysburger.emodiary.core.controller

import com.fiveguysburger.emodiary.core.dto.ChatMessageRequest
import com.fiveguysburger.emodiary.core.dto.ChatMessageResponse
import com.fiveguysburger.emodiary.core.service.ChatRoomService
import org.springframework.http.ResponseEntity
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController()
@RequestMapping("/api/v1")
class DiaryController(
    private val chatRoomService: ChatRoomService,
) {
    @PostMapping("/chatrooms")
    fun createChatRoom(
        @AuthenticationPrincipal userId: String,
    ): ResponseEntity<String> {
        val result = chatRoomService.createChatRoom(userId)
        return ResponseEntity.ok(result)
    }

    @PostMapping("/chatrooms/{chatroomId}")
    fun sendChatMessage(
        @AuthenticationPrincipal userId: String,
        @PathVariable("chatroomId") chatRoomId: String,
        @RequestBody request: ChatMessageRequest,
    ): ResponseEntity<ChatMessageResponse> {
        val llmResponse =
            chatRoomService.sendMessage(
                chatRoomId = chatRoomId,
                userId = userId,
                message = request.message,
            )

        return ResponseEntity.ok(
            ChatMessageResponse(
                chatRoomId = chatRoomId,
                message = llmResponse,
                sender = "LLM",
            ),
        )
    }
}
