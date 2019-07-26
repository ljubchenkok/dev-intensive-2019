package ru.skillbranch.devintensive.utils

import java.net.MalformedURLException
import java.net.URL

object Utils {

    val githubValidHosts = listOf(
        "www.github.com",
        "github.com"
    )
    val githubInvalidPath = listOf(
        "enterprise",
        "features",
        "topics",
        "collections",
        "trending",
        "events",
        "marketplace",
        "pricing",
        "nonprofit",
        "customer-stories",
        "security",
        "login",
        "join"
    )

    fun parseFullName(fullName: String?): Pair<String?, String?> {
        val splitName: List<String>? = fullName?.split(" ")
        var splitNameWithoutNull: ArrayList<String> = ArrayList()
        if (splitName != null) {
            for (name in splitName) {
                if (name.isNotEmpty()) splitNameWithoutNull.add(name)
            }
        }
        var firstName: String? = null
        var lastName: String? = null
        if (splitNameWithoutNull.size > 0) {
            firstName = splitNameWithoutNull.getOrNull(0)
        }
        if (splitNameWithoutNull.size > 1) {
            lastName = splitNameWithoutNull.getOrNull(1)
        }
        return Pair(firstName, lastName)
    }

    fun toInitials(firstName: String?, lastName: String?): String? {
        var first: Char? = null
        var last: Char? = null
        if (firstName != null && firstName.length > 0) {
            first = firstName.capitalize().get(0)
        }
        if (lastName != null && lastName.length > 0) {
            last = lastName.capitalize().get(0)
        }
        var result: String = ""
        if (first != null && first.isLetter()) result += first
        if (last != null && last.isLetter()) result += last
        return if (result.isNotEmpty()) result else null
    }

    var transliteration_map = mapOf<String, String>(
        "а" to "a",
        "б" to "b",
        "в" to "v",
        "г" to "g",
        "д" to "d",
        "е" to "e",
        "ё" to "e",
        "ж" to "zh",
        "з" to "z",
        "и" to "i",
        "й" to "i",
        "к" to "k",
        "л" to "l",
        "м" to "m",
        "н" to "n",
        "о" to "o",
        "п" to "p",
        "р" to "r",
        "с" to "s",
        "т" to "t",
        "у" to "u",
        "ф" to "f",
        "х" to "h",
        "ц" to "c",
        "ч" to "ch",
        "ш" to "sh",
        "щ" to "sh'",
        "ъ" to "",
        "ы" to "i",
        "ь" to "",
        "э" to "e",
        "ю" to "yu",
        "я" to "ya"
    )

    fun transliteration(payload: String, divider: String = " "): String {
        val names = payload.split(" ")
        var result: String = ""
        for (name in names) {
            name.forEach {
                if (transliteration_map.containsKey("${it.toString().toLowerCase()}")) {
                    if (it.isUpperCase()) {
                        result += "${transliteration_map[it.toString().toLowerCase()]?.capitalize()}"
                    } else {
                        result += "${transliteration_map[it.toString()]}"
                    }
                } else {
                    result += "$it"
                }

            }
            result += divider
        }
        return result.substringBeforeLast(divider)
    }

    fun validateUrl(url: String): Boolean {
        var result = true
        var u: URL
        if (url.length < 10) result = false
        else {
            try {
                u = if (url.substring(0, 8) != "https://") {
                    URL("https://$url")
                } else {
                    URL(url)
                }
                with(u) {
                    if (host !in githubValidHosts) result = false
                    if (path.isEmpty() || path in githubInvalidPath) result = false
                    else {
                        var splitPath = path.split("/")
                        if (splitPath.size != 2) result = false
                        for (s in splitPath) {
                            if (s in githubInvalidPath) result = false
                        }
                    }
                }
            } catch (ex: MalformedURLException) {
                result = false
            }
        }
        return result
    }
}