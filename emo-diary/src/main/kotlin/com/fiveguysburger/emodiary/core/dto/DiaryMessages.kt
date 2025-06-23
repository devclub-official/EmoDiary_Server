package com.fiveguysburger.emodiary.core.dto

import com.fasterxml.jackson.annotation.JsonProperty

data class DiaryMessages(
    @JsonProperty("_id")
    val objectId: String,

    @JsonProperty("dailyChatId")
    val dailyChatId: String,

    @JsonProperty("sender")
    val sender: String,

    @JsonProperty("text")
    val text: String,

    @JsonProperty("timestamp")
    val timestamp: String
)

data class Messages(
    @JsonProperty("messages")
    val messages: List<DiaryMessages>
)
