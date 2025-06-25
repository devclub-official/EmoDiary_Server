package com.fiveguysburger.emodiary.core.dto

import com.fasterxml.jackson.annotation.JsonProperty

data class ChatMessageRequest(
    @JsonProperty("message")
    val message: String,
)

data class ChatMessageResponse(
    @JsonProperty("text")
    val text: String,
    @JsonProperty("sender")
    val sender: String = "llm",
)
