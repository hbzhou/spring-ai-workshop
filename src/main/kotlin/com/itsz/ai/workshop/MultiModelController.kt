package com.itsz.ai.workshop

import org.springframework.ai.chat.client.ChatClient
import org.springframework.ai.chat.model.ChatModel
import org.springframework.core.io.ClassPathResource
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/multi-model/")
class MultiModelController(chatModel: ChatModel) {
    private val chatClient = ChatClient.create(chatModel)

    @GetMapping("/picture-content")
    fun getPictureContent(): String? {
        return chatClient.prompt()
            .user {
                it.text("Explain what do you see in the picture?").media(MediaType.IMAGE_PNG, ClassPathResource("static/images/sample.png"))
            }
            .call()
            .content();
    }
}