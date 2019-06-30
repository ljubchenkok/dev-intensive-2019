package ru.skillbranch.devintensive.models

import java.util.*

class TextMessage(id: String, from: User?, chat: Chat, isIncoming: Boolean, date: Date, var text: String?) :
    BaseMessage(id, from, chat, isIncoming, date) {
    override fun formatMessage(): String =
        "$id ${from?.firstName} ${if (isIncoming) "получил" else "отпрвил"} сообщение \"$text\" $date"
}