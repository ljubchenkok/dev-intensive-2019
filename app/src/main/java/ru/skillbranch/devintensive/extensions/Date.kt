package ru.skillbranch.devintensive.extensions

import java.lang.IllegalStateException
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

const val SECOND = 1000L
const val MINUTE = SECOND * 60
const val HOUR = MINUTE * 60
const val DAY = HOUR * 24

fun Date.format(pattern: String = "HH:mm:ss dd.MM.yy"): String? {
    val dateFormat = SimpleDateFormat(pattern, Locale("ru"))
    return dateFormat.format(this)

}

enum class TimeUnits {
    SECOND, MINUTE, HOUR, DAY
}

fun Date.add(value: Int, units: TimeUnits = TimeUnits.SECOND): Date {
    var time = this.time
    time += when (units) {
        TimeUnits.SECOND -> value * SECOND
        TimeUnits.MINUTE -> value * MINUTE
        TimeUnits.HOUR -> value * HOUR
        TimeUnits.DAY -> value * DAY
        else -> throw IllegalStateException("invalid unit")
    }
    this.time = time
    return this
}


val minuteList = arrayListOf("минут", "минуту", "минуты")
val hourList = arrayListOf("часов", "час", "часа")
val dayList = arrayListOf("дней", "день", "дня")

fun getDiffStringWithUnits(diff_unit: Long, units: ArrayList<String>): String {
    when {
        diff_unit in 11..14 -> return "$diff_unit ${units[0]} назад"
        (diff_unit % 10) == 1L -> return "$diff_unit ${units[1]} назад"
        (diff_unit % 10) in 2..4 -> return "$diff_unit ${units[2]} назад"
        else -> return "$diff_unit ${units[0]} назад"
    }
}

fun Date.humanizeDiff(): String {
    val diff = Math.round(((Date().time - this.time) / 1000L).toFloat())
    when (diff) {
        in 0..1 -> return "только что"
        in 1..45 -> return "несколько секунд назад"
        in 45..75 -> return "минуту назад"
        in 76..(60 * 45) -> {
            val diff_unit = Math.round((diff / 60).toDouble())
            return getDiffStringWithUnits(diff_unit, minuteList)

        }
        in (46 * 60)..(75 * 60) -> return "час назад"
        in (75 * 60)..(22 * 3600) -> {
            val diff_unit = Math.round((diff / 3600).toDouble())
            return getDiffStringWithUnits(diff_unit, hourList)
        }
        in (22 * 3600)..(26 * 3600) -> return "день назад"
        in (26 * 3600)..(360 * 24 * 3600) -> {
            val diff_unit = Math.round((diff / (3600 * 24)).toDouble())
            return getDiffStringWithUnits(diff_unit, dayList)
        }
    }
    return "более года назад"

}