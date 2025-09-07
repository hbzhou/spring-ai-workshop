package com.itsz.mcp.client

import org.springframework.boot.CommandLineRunner
import org.springframework.boot.ExitCodeGenerator
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import picocli.CommandLine

@SpringBootApplication
class MCPClientApplication(val factory: CommandLine.IFactory, val chatbotCommand: ChatbotCommand) : CommandLineRunner, ExitCodeGenerator {
    private var exitCode = 0

    override fun run(vararg args: String?) {
       exitCode = CommandLine(chatbotCommand, factory).execute(*args)
    }

    override fun getExitCode(): Int {
       return exitCode
    }
}

fun main(args: Array<String>) {
    runApplication<MCPClientApplication>(*args)
}
