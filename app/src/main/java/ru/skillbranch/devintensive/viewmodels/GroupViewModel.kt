package ru.skillbranch.devintensive.viewmodels

import androidx.lifecycle.*
import ru.skillbranch.devintensive.extensions.mutableLiveData
import ru.skillbranch.devintensive.models.data.UserItem
import ru.skillbranch.devintensive.repositories.GroupRepository

class GroupViewModel : ViewModel() {
    private val query = mutableLiveData("")
    private val groupRepository = GroupRepository
    private val userItems = mutableLiveData(loadUsers())
    private val selectedItems = Transformations.map(userItems) { users ->
        users.filter { it.isSelected }
    }

    fun getUserData(): LiveData<List<UserItem>> {
        val result = MediatorLiveData<List<UserItem>>()
        val filterF = {
            val queryString = query.value!!
            val users = userItems.value!!
            result.value = if(queryString.isEmpty()) users
            else users.filter { it.fullName.contains(queryString, true) }
        }

        result.addSource(userItems) { filterF.invoke()}
        result.addSource(query){filterF.invoke()}
        return result
    }

    fun handleSelectedItems(userId: String) {
        userItems.value = userItems.value!!.map {
            if (it.id == userId) it.copy(isSelected = !it.isSelected)
            else it
        }

    }

    fun getSelectedData(): LiveData<List<UserItem>> = selectedItems

    private fun loadUsers(): List<UserItem> = groupRepository.loadUsers().map { it.toUserItems() }

    fun handleRemoveChip(userId: String) {
        userItems.value = userItems.value!!.map {
            if (it.id == userId) it.copy(isSelected = false)
            else it
        }

    }

    fun handleSearchQuery(text: String?) {
        query.value = text

    }

    fun handleCreateGroup() {
        groupRepository.createChat(selectedItems.value!!)
    }

}