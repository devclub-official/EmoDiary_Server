package com.fiveguysburger.emodiary.core.service.impl

import com.fiveguysburger.emodiary.core.service.ChatRoomService
import com.fiveguysburger.emodiary.mcp.client.tool.DirectToolCall
import org.springframework.stereotype.Service
import java.time.Instant

@Service
class ChatRoomServiceImpl(
    private val directToolCall: DirectToolCall,
) : ChatRoomService {
    override fun createChatRoom(userId: String): String {
        val today = Instant.now()

        val document =
            mapOf(
                "userId" to userId.toInt(),
                "date" to today,
            )
        return directToolCall.insertDocument("chatrooms", document)
    }
}
