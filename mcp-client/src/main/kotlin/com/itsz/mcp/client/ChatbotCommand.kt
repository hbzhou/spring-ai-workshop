package com.itsz.mcp.client

import org.springframework.ai.chat.client.ChatClient
import org.springframework.ai.chat.model.ChatModel
import org.springframework.ai.tool.ToolCallbackProvider
import org.springframework.stereotype.Component
import picocli.CommandLine
import picocli.CommandLine.Command
import picocli.CommandLine.Parameters
import java.util.concurrent.Callable

@Component
@Command(name = "chatbot", aliases = ["chatbot"])
class ChatbotCommand(chatModel: ChatModel, tools: ToolCallbackProvider): Callable<Int>{
    private val chatClient = ChatClient.builder(chatModel).defaultToolCallbacks(tools).build()

    @CommandLine.Option(names = ["--prompt"], description = ["The prompt to send to the chatbot"], required = false)
    private lateinit var prompt: String

    override fun call(): Int {
        chatClient.prompt().user(prompt).stream().content().subscribe {
            print(it)
        }
        return 0
    }
}