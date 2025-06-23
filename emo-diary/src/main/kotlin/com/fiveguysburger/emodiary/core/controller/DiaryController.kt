package com.fiveguysburger.emodiary.core.controller

import com.fiveguysburger.emodiary.core.service.ChatRoomService
import org.springframework.http.ResponseEntity
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController()
@RequestMapping("/api")
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
}
