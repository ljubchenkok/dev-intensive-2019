package ru.skillbranch.devintensive.models

import java.util.*


abstract class BaseMessage(
    val id: String,
    val from: User?,
    val chat: Chat,
    val isIncoming: Boolean = false,
    val date: Date = Date()
) {
    abstract fun formatMessage(): String

    companion object AbstractFactory {
        var lastId = -1
        fun makeMessage(
            from: User?,
            chat: Chat,
            date: Date = Date(),
            isIncoming: Boolean,
            type: TypeMessage = TypeMessage.TEXT,
            payload: Any?
        ): BaseMessage {
            lastId++
            return when(type){
                TypeMessage.IMAGE -> ImageMessage("$lastId", from, chat, isIncoming, date, image = payload as String )
                TypeMessage.TEXT -> TextMessage("$lastId", from, chat, isIncoming, date, text = payload as String)
            }


        }
    }

    enum class TypeMessage {
        TEXT, IMAGE
    }
}