package com.itsz.ai.workshop.service

import com.itsz.ai.workshop.model.WeatherResponse
import com.itsz.ai.workshop.model.ForecastResponse
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.reactive.function.client.awaitBody
import kotlinx.coroutines.runBlocking
import java.util.*

@Service
class WeatherService(
    @Value("\${weather.api.key}")
    private val apiKey: String,
    @Value("\${weather.api.base-url}")
    private val baseUrl: String
) {

    private val webClient = WebClient.builder()
        .baseUrl(baseUrl)
        .build()

    // 城市名称映射（中文到英文）
    private val cityMapping = mapOf(
        "北京" to "Beijing",
        "上海" to "Shanghai",
        "广州" to "Guangzhou",
        "深圳" to "Shenzhen",
        "杭州" to "Hangzhou",
        "南京" to "Nanjing",
        "武汉" to "Wuhan",
        "成都" to "Chengdu",
        "重庆" to "Chongqing",
        "西安" to "Xi'an",
        "天津" to "Tianjin",
        "青岛" to "Qingdao",
        "大连" to "Dalian",
        "厦门" to "Xiamen",
        "苏州" to "Suzhou",
        "长沙" to "Changsha",
        "郑州" to "Zhengzhou",
        "济南" to "Jinan",
        "哈尔滨" to "Harbin",
        "沈阳" to "Shenyang"
    )

    fun getCurrentWeather(city: String): WeatherResponse? {
        return try {
            val englishCity = cityMapping[city] ?: city
            runBlocking {
                webClient.get()
                    .uri { uriBuilder ->
                        uriBuilder
                            .path("/weather")
                            .queryParam("q", "$englishCity,CN")
                            .queryParam("appid", apiKey)
                            .queryParam("units", "metric")
                            .queryParam("lang", "zh_cn")
                            .build()
                    }
                    .retrieve()
                    .awaitBody<WeatherResponse>()
            }
        } catch (e: Exception) {
            println("获取天气数据失败: ${e.message}")
            null
        }
    }

    fun getWeatherForecast(city: String): ForecastResponse? {
        return try {
            val englishCity = cityMapping[city] ?: city
            runBlocking {
                webClient.get()
                    .uri { uriBuilder ->
                        uriBuilder
                            .path("/forecast")
                            .queryParam("q", "$englishCity,CN")
                            .queryParam("appid", apiKey)
                            .queryParam("units", "metric")
                            .queryParam("lang", "zh_cn")
                            .build()
                    }
                    .retrieve()
                    .awaitBody<ForecastResponse>()
            }
        } catch (e: Exception) {
            println("获取天气预报数据失败: ${e.message}")
            null
        }
    }

    // 将英文天气描述转换为中文
    fun translateWeatherDescription(description: String): String {
        val translations = mapOf(
            "clear sky" to "晴空",
            "few clouds" to "少云",
            "scattered clouds" to "多云",
            "broken clouds" to "阴天",
            "overcast clouds" to "阴天",
            "light rain" to "小雨",
            "moderate rain" to "中雨",
            "heavy intensity rain" to "大雨",
            "very heavy rain" to "暴雨",
            "light snow" to "小雪",
            "snow" to "雪",
            "heavy snow" to "大雪",
            "mist" to "薄雾",
            "fog" to "雾",
            "haze" to "霾",
            "thunderstorm" to "雷暴"
        )
        return translations[description.lowercase(Locale.getDefault())] ?: description
    }

    // 根据风速获取风力等级
    fun getWindLevel(windSpeed: Double): String {
        return when {
            windSpeed < 0.3 -> "0级 无风"
            windSpeed < 1.6 -> "1级 软风"
            windSpeed < 3.4 -> "2级 轻风"
            windSpeed < 5.5 -> "3级 微风"
            windSpeed < 8.0 -> "4级 和风"
            windSpeed < 10.8 -> "5级 清劲风"
            windSpeed < 13.9 -> "6级 强风"
            windSpeed < 17.2 -> "7级 疾风"
            windSpeed < 20.8 -> "8级 大风"
            windSpeed < 24.5 -> "9级 烈风"
            windSpeed < 28.5 -> "10级 狂风"
            windSpeed < 32.7 -> "11级 暴风"
            else -> "12级 飓风"
        }
    }

    // 根据风向角度获取风向
    fun getWindDirection(degree: Int?): String {
        if (degree == null) return "无风向"
        return when ((degree + 22.5) % 360) {
            in 0.0..44.9 -> "北风"
            in 45.0..89.9 -> "东北风"
            in 90.0..134.9 -> "东风"
            in 135.0..179.9 -> "东南风"
            in 180.0..224.9 -> "南风"
            in 225.0..269.9 -> "西南风"
            in 270.0..314.9 -> "西风"
            in 315.0..359.9 -> "西北风"
            else -> "北风"
        }
    }
}
