package ru.skillbranch.devintensive.extensions

fun String.truncate(size: Int = 16) =
    if (this.trim().length > size)
        this.trim().dropLast(this.trim().length - size).trim() + "..."
    else this.trim()


fun String.stripHtml() = this
    .replace("<[^>]+>".toRegex(), "")
    .replace("&[a-z;#\\d]+;".toRegex(), "")
    .replace(" {2,}".toRegex(), " ")
