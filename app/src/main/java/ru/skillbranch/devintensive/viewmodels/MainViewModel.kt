package ru.skillbranch.devintensive.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import ru.skillbranch.devintensive.extensions.mutableLiveData
import ru.skillbranch.devintensive.extensions.shortFormat
import ru.skillbranch.devintensive.models.data.Chat
import ru.skillbranch.devintensive.models.data.ChatItem
import ru.skillbranch.devintensive.models.data.ChatType
import ru.skillbranch.devintensive.repositories.ChatRepository

internal class MainViewModel : ViewModel() {

    private val query = mutableLiveData("")
    private val chatRepository = ChatRepository

    private val chats = Transformations.map(chatRepository.loadChats()) { chats ->
        if (chats.none { it.isArchived }) {
            return@map chats.filter { !it.isArchived }
                .map { it.toChatItem() }.sortedBy { it.id.toInt() }
        } else {
            val chatItem =
                getArchiveSummary(chats.filter { it.isArchived })
            val list = mutableListOf<ChatItem>()
            list.add(0, chatItem)
            list.addAll(chats.filter { !it.isArchived }
                .map { it.toChatItem() }.sortedBy { it.id.toInt() })

            return@map list
        }

    }

    private fun getArchiveSummary(chats: List<Chat>): ChatItem {
        var count = 0
        chats.forEach() {
            count += it.unreadableMessageCount()
        }
        val lastChat =
            chats.lastOrNull { it.unreadableMessageCount() != 0 } ?: chats.last()

        return ChatItem(
            id = "archive",
            author = lastChat.lastMessageShort().second,
            messageCount = count,
            title = "Архив",
            avatar = null,
            initials = "",
            chatType = ChatType.ARCHIVE,
            isOnline = false,
            lastMessageDate = lastChat.lastMessageDate()?.shortFormat(),
            shortDescription = lastChat.lastMessageShort().first
        )

    }


    fun getChatData(): LiveData<List<ChatItem>> {
        val result = MediatorLiveData<List<ChatItem>>()
        val filterF = {
            val queryString = query.value!!
            result.value = if (queryString.isEmpty()) chats.value
            else chats.value?.filter { it.title.contains(queryString, true) }
        }
        result.addSource(chats) { filterF.invoke() }
        result.addSource(query) { filterF.invoke() }
        return result
    }


    fun addToArchive(id: String) {
        val chat = ChatRepository.find(id)
        chat ?: return
        chatRepository.update(chat.copy(isArchived = true))
    }

    fun restoreFromArchive(id: String) {
        val chat = ChatRepository.find(id)
        chat ?: return
        chatRepository.update(chat.copy(isArchived = false))

    }

    fun handleSearchQuery(text: String?) {
        query.value = text

    }

}
