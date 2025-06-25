package com.fiveguysburger.emodiary.client

import org.springframework.ai.chat.client.ChatClient
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class ChatClinetConfig {
    @Bean
    fun chatClient(builder: ChatClient.Builder): ChatClient {
        return builder
            .defaultSystem(
                "너는 사용자가 일기를 풍요롭게 작성할 수 있게 도와줄 도우미야. " +
                    "사용자가 하루를 잘 돌아보며 그것을 일기로 작성할 수 있도록" +
                    "사용자의 감정에 공감하면서 유도 질문을 해.",
            )
            .build()
    }
}
