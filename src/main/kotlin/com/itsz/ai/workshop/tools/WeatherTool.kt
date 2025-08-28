package com.itsz.ai.workshop.tools

import com.itsz.ai.workshop.service.WeatherService
import org.springframework.ai.tool.annotation.Tool
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import kotlin.math.roundToInt

@Component
class WeatherTool {

    @Autowired
    private lateinit var weatherService: WeatherService

    @Tool(description = "Get current weather information for a specified city in China")
    fun getRecentWeather(city: String = "北京"): String {
        val weatherData = weatherService.getCurrentWeather(city)

        return if (weatherData != null) {
            val temperature = weatherData.main.temp.roundToInt()
            val feelsLike = weatherData.main.feelsLike.roundToInt()
            val humidity = weatherData.main.humidity
            val pressure = weatherData.main.pressure
            val windSpeed = weatherData.wind.speed
            val windDirection = weatherService.getWindDirection(weatherData.wind.deg)
            val windLevel = weatherService.getWindLevel(windSpeed)
            val description = weatherService.translateWeatherDescription(weatherData.weather.firstOrNull()?.description ?: "")
            val cloudiness = weatherData.clouds.all

            """
            🌤️ ${weatherData.name}当前天气:
            📅 日期: ${LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy年MM月dd日"))}
            🌡️ 温度: ${temperature}°C (体感温度: ${feelsLike}°C)
            ☁️ 天气: $description
            💧 湿度: ${humidity}%
            📊 气压: ${pressure} hPa
            ☁️ 云量: ${cloudiness}%
            💨 风况: $windDirection ${windLevel}
            
            📍 位置: ${weatherData.name}, ${weatherData.sys.country}
            🌅 日出: ${formatTime(weatherData.sys.sunrise)}
            🌇 日落: ${formatTime(weatherData.sys.sunset)}
            ⏰ 更新时间: ${LocalDateTime.now().format(DateTimeFormatter.ofPattern("HH:mm"))}
            
            📡 数据来源: OpenWeatherMap
            """.trimIndent()
        } else {
            "❌ 抱歉，无法获取${city}的天气信息，请检查城市名称是否正确或稍后重试。"
        }
    }

    @Tool(description = "Get weather forecast for the next few days for a specified city in China")
    fun getWeatherForecast(city: String = "北京", days: Int = 5): String {
        val forecastData = weatherService.getWeatherForecast(city)

        return if (forecastData != null) {
            val forecast = StringBuilder()
            forecast.append("🌈 ${forecastData.city.name}未来${days}天天气预报:\n\n")

            // 按日期分组预报数据
            val dailyForecasts = forecastData.list
                .take(days * 8) // OpenWeatherMap返回每3小时一次的数据，每天8次
                .groupBy { item ->
                    LocalDateTime.ofInstant(
                        Instant.ofEpochSecond(item.dt),
                        ZoneId.systemDefault()
                    ).toLocalDate()
                }
                .entries.take(days)

            dailyForecasts.forEachIndexed { index, (date, items) ->
                val dayLabel = when (index) {
                    0 -> "今天"
                    1 -> "明天"
                    2 -> "后天"
                    else -> date.format(DateTimeFormatter.ofPattern("MM月dd日"))
                }

                // 取当天的最高温和最低温
                val maxTemp = items.maxOf { it.main.tempMax }.roundToInt()
                val minTemp = items.minOf { it.main.tempMin }.roundToInt()

                // 取白天时段的天气状况（一般取中午12点左右的数据）
                val dayTimeItem = items.find {
                    val hour = LocalDateTime.ofInstant(
                        Instant.ofEpochSecond(it.dt),
                        ZoneId.systemDefault()
                    ).hour
                    hour in 10..14
                } ?: items.first()

                val description = weatherService.translateWeatherDescription(
                    dayTimeItem.weather.firstOrNull()?.description ?: ""
                )
                val windLevel = weatherService.getWindLevel(dayTimeItem.wind.speed)
                val humidity = dayTimeItem.main.humidity

                forecast.append("📅 $dayLabel (${date.format(DateTimeFormatter.ofPattern("MM月dd日"))})\n")
                forecast.append("🌡️ 温度: ${minTemp}°C ~ ${maxTemp}°C\n")
                forecast.append("☁️ 天气: $description\n")
                forecast.append("💨 风力: $windLevel\n")
                forecast.append("💧 湿度: ${humidity}%\n")

                if (index < dailyForecasts.size - 1) forecast.append("\n")
            }

            forecast.append("\n📡 数据来源: OpenWeatherMap")
            forecast.toString()
        } else {
            "❌ 抱歉，无法获取${city}的天气预报信息，请检查城市名称是否正确或稍后重试。"
        }
    }

    @Tool(description = "Get weather information for multiple cities at once")
    fun getMultiCityWeather(cities: List<String> = listOf("北京", "上海", "广州", "深圳")): String {
        val result = StringBuilder()
        result.append("🌍 多城市天气概况:\n\n")

        cities.forEachIndexed { index, city ->
            val weatherData = weatherService.getCurrentWeather(city)

            if (weatherData != null) {
                val temperature = weatherData.main.temp.roundToInt()
                val description = weatherService.translateWeatherDescription(
                    weatherData.weather.firstOrNull()?.description ?: ""
                )
                result.append("📍 ${weatherData.name}: ${temperature}°C $description")
            } else {
                result.append("📍 $city: 数据获取失败")
            }

            if (index < cities.size - 1) result.append("\n")
        }

        result.append("\n\n📡 数据来源: OpenWeatherMap")
        return result.toString()
    }

    private fun formatTime(timestamp: Long): String {
        return LocalDateTime.ofInstant(
            Instant.ofEpochSecond(timestamp),
            ZoneId.systemDefault()
        ).format(DateTimeFormatter.ofPattern("HH:mm"))
    }
}
