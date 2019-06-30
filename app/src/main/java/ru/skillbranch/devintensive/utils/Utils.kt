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
}