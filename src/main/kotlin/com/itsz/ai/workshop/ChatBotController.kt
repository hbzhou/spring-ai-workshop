package com.itsz.ai.workshop

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.reactive.asFlow
import org.springframework.ai.chat.client.ChatClient
import org.springframework.ai.chat.model.ChatModel
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/chatbot/")
class ChatBotController(chatModel: ChatModel) {
    private val chatClient = ChatClient.create(chatModel)

    @GetMapping("/chat")
    fun chat(@RequestParam prompt: String): String? = chatClient.prompt().user(prompt).call().content()

    @GetMapping("/stream")
    fun streamChat(@RequestParam prompt: String): Flow<String> = chatClient.prompt().user(prompt).stream().content().asFlow()
}