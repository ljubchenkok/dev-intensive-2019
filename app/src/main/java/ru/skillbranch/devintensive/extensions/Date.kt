package ru.skillbranch.devintensive.extensions

import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.roundToLong


fun Date.format(pattern: String = "HH:mm:ss dd.MM.yy"): String? {
    val dateFormat = SimpleDateFormat(pattern, Locale("ru"))
    return dateFormat.format(this)

}

val secondDeclensionList = arrayListOf("секунд", "секунду", "секунды", "секунда")
val minuteDeclensionList = arrayListOf("минут", "минуту", "минуты", "минута")
val hourDeclensionList = arrayListOf("часов", "час", "часа", "час")
val dayDeclensionList = arrayListOf("дней", "день", "дня", "день")

enum class TimeUnits(val value: Long) {
    SECOND(1000L),
    MINUTE(SECOND.value * 60L),
    HOUR(MINUTE.value * 60L),
    DAY(HOUR.value * 24L);
    fun plural(duration: Long):String{
        return when(this){
            SECOND -> getDurationStringWithUnits(duration, secondDeclensionList, isPlural = true)
            MINUTE -> getDurationStringWithUnits(duration, minuteDeclensionList, isPlural = true)
            HOUR -> getDurationStringWithUnits(duration, hourDeclensionList, isPlural = true)
            DAY -> getDurationStringWithUnits(duration, dayDeclensionList, isPlural = true)
        }
    }
}

fun Date.add(value: Int, units: TimeUnits = TimeUnits.SECOND): Date {
    this.time += value * units.value
    return this
}


fun getDurationStringWithUnits(value: Long, units: ArrayList<String>, isPast: Boolean = true, isPlural: Boolean=false): String {
    val index = when {
        value %100 in 11..14 -> 0
        value % 10 == 1L -> 1
        value % 10 in 2..4 -> 2
        else -> 0
    }
    return if (isPlural) "$value ${units[index]}" else if (isPast) "$value ${units[index]} назад" else "через $value ${units[index]}"
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
                return getDurationStringWithUnits(diffUnit, minuteDeclensionList)

            }
            in (46 * 60)..(75 * 60) -> return "час назад"
            in (75 * 60)..(22 * 3600) -> {
                val diffUnit = (diff / 3600).toDouble().roundToLong()
                return getDurationStringWithUnits(diffUnit, hourDeclensionList)
            }
            in (22 * 3600)..(26 * 3600) -> return "день назад"
            in (26 * 3600)..(360 * 24 * 3600) -> {
                val diffUnit = (diff / (3600 * 24)).toDouble().roundToLong()
                return getDurationStringWithUnits(diffUnit, dayDeclensionList)
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
                return getDurationStringWithUnits(diffUnit, minuteDeclensionList, false)

            }
            in (46 * 60)..(75 * 60) -> return "через час"
            in (75 * 60)..(22 * 3600) -> {
                val diffUnit = (diff / 3600).toDouble().roundToLong()
                return getDurationStringWithUnits(diffUnit, hourDeclensionList, false)
            }
            in (22 * 3600)..(26 * 3600) -> return "через день"
            in (26 * 3600)..(360 * 24 * 3600) -> {
                val diffUnit = (diff / (3600 * 24)).toDouble().roundToLong()
                return getDurationStringWithUnits(diffUnit, dayDeclensionList, false)
            }
            else -> return "более чем через год"
        }

    }


}


fun Date.shortFormat(): String? {
    val pattern = if(this.isSameDay(Date())) "HH:mm" else "dd.MM.yy"
    val dateFormat = SimpleDateFormat(pattern, Locale("ru"))
    return dateFormat.format(this)

}

fun Date.isSameDay(date: Date): Boolean{
    val day1 = this.time/ TimeUnits.DAY.value
    val day2 = date.time/ TimeUnits.DAY.value
    return  day1 == day2
}