package com.itsz.ai.workshop

import org.springframework.ai.chat.client.ChatClient
import org.springframework.ai.chat.client.entity
import org.springframework.ai.chat.model.ChatModel
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/structure-output/")
class StructureOutputController(chatModel: ChatModel) {
    private val chatClient = ChatClient.create(chatModel)

    @GetMapping("/actorFilms")
    fun getActorFilms(@RequestParam actor: String): ActorFilms? {
        return chatClient.prompt().user { it.text("Generate the filmography of 5 movies for ${actor}.") }
            .call()
            .entity<ActorFilms>()
    }
}

data class ActorFilms(val actor: String, val films: List<String>)