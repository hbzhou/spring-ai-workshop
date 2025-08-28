package com.itsz.ai.workshop.tools

import org.springframework.ai.tool.annotation.Tool
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class ChinaHolidayTool {

    @Tool(description = "Get the nearest upcoming holidays in China from the current date")
    fun getNearestChinaHolidays(): String {
        val currentDate = LocalDate.now()
        val holidays = getChinaHolidays2025()

        val upcomingHolidays = holidays.filter { it.date.isAfter(currentDate) || it.date.isEqual(currentDate) }
            .sortedBy { it.date }
            .take(5)

        if (upcomingHolidays.isEmpty()) {
            return "没有找到即将到来的中国节假日信息。"
        }

        val formatter = DateTimeFormatter.ofPattern("yyyy年MM月dd日")
        return upcomingHolidays.joinToString("\n") { holiday ->
            val daysUntil = java.time.temporal.ChronoUnit.DAYS.between(currentDate, holiday.date)
            val timeInfo = when {
                daysUntil == 0L -> "今天"
                daysUntil == 1L -> "明天"
                daysUntil < 30 -> "${daysUntil}天后"
                else -> ""
            }
            "${holiday.name}: ${holiday.date.format(formatter)} ${if (timeInfo.isNotEmpty()) "($timeInfo)" else ""}"
        }
    }

    private fun getChinaHolidays2025(): List<Holiday> {
        return listOf(
            // 2025年节假日
            Holiday("教师节", LocalDate.of(2025, 9, 10)),
            Holiday("中秋节", LocalDate.of(2025, 10, 6)),
            Holiday("国庆节", LocalDate.of(2025, 10, 1)),
            Holiday("重阳节", LocalDate.of(2025, 10, 29)),
            Holiday("万圣节", LocalDate.of(2025, 10, 31)),
            Holiday("双十一购物节", LocalDate.of(2025, 11, 11)),
            Holiday("感恩节", LocalDate.of(2025, 11, 27)),
            Holiday("双十二购物节", LocalDate.of(2025, 12, 12)),
            Holiday("冬至", LocalDate.of(2025, 12, 21)),
            Holiday("圣诞节", LocalDate.of(2025, 12, 25)),
            Holiday("元旦", LocalDate.of(2026, 1, 1)),
            Holiday("腊八节", LocalDate.of(2026, 1, 27)),
            Holiday("小年", LocalDate.of(2026, 2, 9)),
            Holiday("除夕", LocalDate.of(2026, 2, 16)),
            Holiday("春节", LocalDate.of(2026, 2, 17)),
            Holiday("元宵节", LocalDate.of(2026, 3, 3)),
            Holiday("妇女节", LocalDate.of(2026, 3, 8)),
            Holiday("植树节", LocalDate.of(2026, 3, 12)),
            Holiday("消费者权益日", LocalDate.of(2026, 3, 15)),
            Holiday("清明节", LocalDate.of(2026, 4, 5)),
            Holiday("劳动节", LocalDate.of(2026, 5, 1)),
            Holiday("青年节", LocalDate.of(2026, 5, 4)),
            Holiday("母亲节", LocalDate.of(2026, 5, 10)),
            Holiday("儿童节", LocalDate.of(2026, 6, 1)),
            Holiday("端午节", LocalDate.of(2026, 6, 20)),
            Holiday("建党节", LocalDate.of(2026, 7, 1)),
            Holiday("建军节", LocalDate.of(2026, 8, 1)),
            Holiday("七夕节", LocalDate.of(2026, 8, 19)),
            Holiday("教师节", LocalDate.of(2026, 9, 10))
        )
    }

    data class Holiday(
        val name: String,
        val date: LocalDate
    )
}
