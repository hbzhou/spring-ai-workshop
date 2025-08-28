package com.itsz.ai.workshop.model

import com.fasterxml.jackson.annotation.JsonProperty

data class WeatherResponse(
    val coord: Coordinates,
    val weather: List<WeatherCondition>,
    val main: MainWeatherData,
    val wind: Wind,
    val clouds: Clouds,
    val sys: SystemData,
    val name: String,
    val cod: Int
)

data class Coordinates(
    val lon: Double,
    val lat: Double
)

data class WeatherCondition(
    val id: Int,
    val main: String,
    val description: String,
    val icon: String
)

data class MainWeatherData(
    val temp: Double,
    @JsonProperty("feels_like")
    val feelsLike: Double,
    @JsonProperty("temp_min")
    val tempMin: Double,
    @JsonProperty("temp_max")
    val tempMax: Double,
    val pressure: Int,
    val humidity: Int
)

data class Wind(
    val speed: Double,
    val deg: Int? = null,
    val gust: Double? = null
)

data class Clouds(
    val all: Int
)

data class SystemData(
    val country: String,
    val sunrise: Long,
    val sunset: Long
)

// 5天天气预报数据模型
data class ForecastResponse(
    val cod: String,
    val message: Int,
    val cnt: Int,
    val list: List<ForecastItem>,
    val city: CityInfo
)

data class ForecastItem(
    val dt: Long,
    val main: MainWeatherData,
    val weather: List<WeatherCondition>,
    val clouds: Clouds,
    val wind: Wind,
    @JsonProperty("dt_txt")
    val dtTxt: String
)

data class CityInfo(
    val id: Int,
    val name: String,
    val coord: Coordinates,
    val country: String,
    val population: Int,
    val timezone: Int,
    val sunrise: Long,
    val sunset: Long
)
