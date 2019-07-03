package ru.skillbranch.devintensive.extensions

import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.roundToLong


fun Date.format(pattern: String = "HH:mm:ss dd.MM.yy"): String? {
    val dateFormat = SimpleDateFormat(pattern, Locale("ru"))
    return dateFormat.format(this)

}

enum class TimeUnits(val value: Long) {
    SECOND(1000L),
    MINUTE(SECOND.value * 60L),
    HOUR(MINUTE.value * 60L),
    DAY(HOUR.value * 24L)
}

fun Date.add(value: Int, units: TimeUnits = TimeUnits.SECOND): Date {
    this.time += value * units.value
    return this
}


val minuteList = arrayListOf("минут", "минуту", "минуты")
val hourList = arrayListOf("часов", "час", "часа")
val dayList = arrayListOf("дней", "день", "дня")

fun getDiffStringWithUnits(diff_unit: Long, units: ArrayList<String>, isPast: Boolean = true): String {
    var index = 0
    index = when {
        diff_unit in 11..14 -> 0
        (diff_unit % 10) == 1L -> 1
        (diff_unit % 10) in 2..4 -> 2
        else -> 0
    }
    return if (isPast) "$diff_unit ${units[index]} назад" else "через $diff_unit ${units[index]}"
}

fun Date.humanizeDiff(): String {
    var diff = ((Date().time - this.time) / 1000L).toFloat().roundToLong()
    if (diff >= 0) {
        when (diff) {
            in 0..1 -> return "только что"
            in 1..45 -> return "несколько секунд назад"
            in 45..75 -> return "минуту назад"
            in 76..(60 * 45) -> {
                val diffUnit = (diff / 60).toDouble().roundToLong()
                return getDiffStringWithUnits(diffUnit, minuteList)

            }
            in (46 * 60)..(75 * 60) -> return "час назад"
            in (75 * 60)..(22 * 3600) -> {
                val diffUnit = (diff / 3600).toDouble().roundToLong()
                return getDiffStringWithUnits(diffUnit, hourList)
            }
            in (22 * 3600)..(26 * 3600) -> return "день назад"
            in (26 * 3600)..(360 * 24 * 3600) -> {
                val diffUnit = (diff / (3600 * 24)).toDouble().roundToLong()
                return getDiffStringWithUnits(diffUnit, dayList)
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
                val diffUnit = (diff / 60).toDouble().roundToLong()
                return getDiffStringWithUnits(diffUnit, minuteList, false)

            }
            in (46 * 60)..(75 * 60) -> return "через час"
            in (75 * 60)..(22 * 3600) -> {
                val diffUnit = (diff / 3600).toDouble().roundToLong()
                return getDiffStringWithUnits(diffUnit, hourList, false)
            }
            in (22 * 3600)..(26 * 3600) -> return "через день"
            in (26 * 3600)..(360 * 24 * 3600) -> {
                val diffUnit = (diff / (3600 * 24)).toDouble().roundToLong()
                return getDiffStringWithUnits(diffUnit, dayList, false)
            }
            else -> return "более чем через год"
        }

    }

}