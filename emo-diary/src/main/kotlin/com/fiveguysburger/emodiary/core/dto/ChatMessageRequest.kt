package com.fiveguysburger.emodiary.core.dto

import com.fasterxml.jackson.annotation.JsonProperty

data class ChatMessageRequest(
    @JsonProperty("message")
    val message: String,
)

data class ChatMessageResponse(
    @JsonProperty("text")
    val text: String, // LLM이 생성한 순수 텍스트

    @JsonProperty("sender")
    val sender: String = "llm" // 보낸 사람은 항상 'llm'
)
