package com.itsz.ai.workshop.mcp

import com.itsz.ai.workshop.tools.DateTimeTool
import com.itsz.ai.workshop.tools.ChinaHolidayTool
import com.itsz.ai.workshop.tools.WeatherTool
import org.springframework.ai.tool.ToolCallbackProvider
import org.springframework.ai.tool.method.MethodToolCallbackProvider
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class MCPServerConfiguration {

    @Autowired
    private lateinit var weatherTool: WeatherTool

    @Bean
    fun dateTime(): ToolCallbackProvider {
        return MethodToolCallbackProvider.builder().toolObjects(DateTimeTool()).build()
    }

    @Bean
    fun chinaHoliday(): ToolCallbackProvider {
        return MethodToolCallbackProvider.builder().toolObjects(ChinaHolidayTool()).build()
    }

    @Bean
    fun weather(): ToolCallbackProvider {
        return MethodToolCallbackProvider.builder().toolObjects(weatherTool).build()
    }
}