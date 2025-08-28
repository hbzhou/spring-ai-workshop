package com.itsz.ai.workshop.tools

import org.springframework.ai.tool.annotation.Tool
import org.springframework.context.i18n.LocaleContextHolder
import java.time.LocalDateTime

class DateTimeTool {

    @Tool(description = "Get the current date and time in the user's timezone")
    fun currentDateTime(): String {
        return LocalDateTime.now().atZone(LocaleContextHolder.getTimeZone().toZoneId()).toString()
    }
}