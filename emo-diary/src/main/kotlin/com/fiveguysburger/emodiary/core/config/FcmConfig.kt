package com.fiveguysburger.emodiary.core.config

import com.google.auth.oauth2.GoogleCredentials
import com.google.firebase.FirebaseApp
import com.google.firebase.FirebaseOptions
import com.google.firebase.messaging.FirebaseMessaging
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.io.ClassPathResource
import java.io.IOException

@Configuration
class FcmConfig {
    @Bean
    @Throws(IOException::class)
    fun firebaseApp(): FirebaseApp {
        try {
            val resource = ClassPathResource("emoDiary-firebase-key.json")
            if (!resource.exists()) {
                throw IOException("Firebase 서비스 계정 키 파일을 찾을 수 없습니다: emoDiary-firebase-key.json")
            }

            val serviceAccount = resource.inputStream
            val bufferedStream = serviceAccount.readBytes().inputStream() // 스트림 복사
            val options =
                FirebaseOptions
                    .builder()
                    .setCredentials(GoogleCredentials.fromStream(bufferedStream))
                    .build()

            return if (FirebaseApp.getApps().isEmpty()) {
                FirebaseApp.initializeApp(options)
            } else {
                FirebaseApp.getInstance()
            }
        } catch (e: IOException) {
            throw RuntimeException("Firebase 초기화 실패:", e)
        }
    }

    @Bean
    fun firebaseMessaging(firebaseApp: FirebaseApp): FirebaseMessaging = FirebaseMessaging.getInstance(firebaseApp)
}
