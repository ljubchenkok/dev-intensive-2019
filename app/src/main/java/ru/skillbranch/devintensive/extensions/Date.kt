package ru.skillbranch.devintensive.extensions

import java.text.SimpleDateFormat
import java.util.*

const val SECOND = 1000L
const val MINUTE = SECOND * 60L
const val HOUR = MINUTE * 60L
const val DAY = HOUR * 24L

fun Date.format(pattern: String = "HH:mm:ss dd.MM.yy"): String? {
    val dateFormat = SimpleDateFormat(pattern, Locale("ru"))
    return dateFormat.format(this)

}

enum class TimeUnits {
    SECOND, MINUTE, HOUR, DAY
}

fun Date.add(value: Int, units: TimeUnits = TimeUnits.SECOND): Date {
    this.time += when (units) {
        TimeUnits.SECOND -> value * SECOND
        TimeUnits.MINUTE -> value * MINUTE
        TimeUnits.HOUR -> value * HOUR
        TimeUnits.DAY -> value * DAY
    }
    return this
}


val minuteList = arrayListOf("минут", "минуту", "минуты")
val hourList = arrayListOf("часов", "час", "часа")
val dayList = arrayListOf("дней", "день", "дня")

fun getDiffStringWithUnits(diff_unit: Long, units: ArrayList<String>, isPast: Boolean = true): String {
    var result = ""
    if (isPast) {
        when {
            diff_unit in 11..14 -> result = "$diff_unit ${units[0]} назад"
            (diff_unit % 10) == 1L -> result = "$diff_unit ${units[1]} назад"
            (diff_unit % 10) in 2..4 -> result = "$diff_unit ${units[2]} назад"
            else -> result = "$diff_unit ${units[0]} назад"
        }
    } else {
        when {
            diff_unit in 11..14 -> result = "через $diff_unit ${units[0]}"
            (diff_unit % 10) == 1L -> result = "через $diff_unit ${units[1]}"
            (diff_unit % 10) in 2..4 -> result = "через $diff_unit ${units[2]}"
            else -> result = "через $diff_unit ${units[0]}"
        }
    }
    return result
}

fun Date.humanizeDiff(): String {
    var diff = Math.round(((Date().time - this.time) / 1000L).toFloat())
    if (diff >= 0) {
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
            else -> return "более года назад"

        }
    } else {
        diff = -diff
        when (diff) {
            in 0..1 -> return "через несколько секунд"
            in 1..45 -> return "через несколько секунд"
            in 45..75 -> return "через минуту"
            in 76..(60 * 45) -> {
                val diff_unit = Math.round((diff / 60).toDouble())
                return getDiffStringWithUnits(diff_unit, minuteList, false)

            }
            in (46 * 60)..(75 * 60) -> return "через час"
            in (75 * 60)..(22 * 3600) -> {
                val diff_unit = Math.round((diff / 3600).toDouble())
                return getDiffStringWithUnits(diff_unit, hourList, false)
            }
            in (22 * 3600)..(26 * 3600) -> return "через день"
            in (26 * 3600)..(360 * 24 * 3600) -> {
                val diff_unit = Math.round((diff / (3600 * 24)).toDouble())
                return getDiffStringWithUnits(diff_unit, dayList, false)
            }
            else -> return "более чем через год"
        }

    }

}