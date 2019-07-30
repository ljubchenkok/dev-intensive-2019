package ru.skillbranch.devintensive.viewmodels

import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ru.skillbranch.devintensive.models.Profile
import ru.skillbranch.devintensive.repositories.PreferencesRepository
import ru.skillbranch.devintensive.utils.Utils

class ProfileViewModel: ViewModel() {
    private val repository: PreferencesRepository = PreferencesRepository
    private val profileData = MutableLiveData<Profile>()
    private var appTheme = MutableLiveData<Int>()
    private var isRepoValid = MutableLiveData<Boolean>()

    init {
        profileData.value = repository.getProfile()
        appTheme.value = repository.getAppTheme()
        onRepoChanged(repository.getProfile().repository)
    }

    fun getProfileData():LiveData<Profile> = profileData

    fun getRepoStatus():LiveData<Boolean> = isRepoValid

    fun saveProfileData(profile: Profile){
        if(isRepoValid.value == false) profile.repository = ""
        repository.saveProfile(profile)
        profileData.value = profile
    }

    fun switchTheme() {
        if(appTheme.value == AppCompatDelegate.MODE_NIGHT_YES){
            appTheme.value = AppCompatDelegate.MODE_NIGHT_NO
        }
        else {
            appTheme.value = AppCompatDelegate.MODE_NIGHT_YES
        }
        repository.saveAppTheme(appTheme.value!!)
    }

    fun getAppTheme():LiveData<Int> = appTheme

    fun onRepoChanged(repo: String) {
        isRepoValid.value = (Utils.validateUrl(repo))
    }
}