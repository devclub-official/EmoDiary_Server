package com.fiveguysburger.emodiary.core.dto

import com.fasterxml.jackson.annotation.JsonProperty

data class CreateDiaryRoomResponse(
    @JsonProperty("daily_chat_id")
    val dailyChatId: String
)