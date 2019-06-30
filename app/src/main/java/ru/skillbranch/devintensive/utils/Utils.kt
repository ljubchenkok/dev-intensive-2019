package ru.skillbranch.devintensive.utils

object Utils {
    fun parseFullName(fullName: String?): Pair<String?, String?> {
        val splitName: List<String>? = fullName?.split(" ")
        var firstName = splitName?.getOrNull(0)
        var secondName = splitName?.getOrNull(1)
        if (firstName?.length == 0) firstName = null
        if (secondName?.length == 0) secondName = null
        return Pair(firstName, secondName)
    }

    fun toInitials(firstName:String?, lastName:String?):String?{
        val first = firstName?.capitalize()?.get(0)
        val last = lastName?.capitalize()?.get(0)
        var result:String? = null
        if(first != null && first?.isLetter()) result="$first"
        if(last != null && last.isLetter()) result="$last"
        return result

    }
}