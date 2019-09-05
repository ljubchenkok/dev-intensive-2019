package ru.skillbranch.devintensive.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import ru.skillbranch.devintensive.extensions.mutableLiveData
import ru.skillbranch.devintensive.models.data.ChatItem
import ru.skillbranch.devintensive.repositories.ChatRepository
import ru.skillbranch.devintensive.utils.DataGenerator

internal class MainViewModel : ViewModel() {

    private val chatRepository = ChatRepository
    private val chats = Transformations.map(chatRepository.loadChats()) { chats ->
        return@map chats.filter { !it.isArchived }
            .map { it.toChatItem() }.sortedBy { it.id.toInt() }

    }

    fun getChatData(): LiveData<List<ChatItem>> {
        return chats
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

}
