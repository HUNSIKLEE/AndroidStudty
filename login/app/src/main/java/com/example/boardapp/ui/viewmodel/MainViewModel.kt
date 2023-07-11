// MainViewModel.kt

package com.example.boardapp.ui.viewmodel

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import androidx.activity.result.ActivityResultLauncher
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.boardapp.data.model.ProfileData
import com.example.boardapp.data.dao.ProfileDataDao
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainViewModel : ViewModel() {

    private lateinit var profileDataDao: ProfileDataDao
    private lateinit var imageResult: ActivityResultLauncher<Intent>
    val profileList: MutableLiveData<List<ProfileData>> = MutableLiveData()

    fun initialize(profileDataDao: ProfileDataDao, imageResult: ActivityResultLauncher<Intent>) {
        this.profileDataDao = profileDataDao
        this.imageResult = imageResult
    }

    fun loadProfileList() {
        viewModelScope.launch(Dispatchers.IO) {
            val profileList = profileDataDao.getAll()
            launch(Dispatchers.Main) {
                this@MainViewModel.profileList.value = profileList
            }
        }
    }

    fun addProfile(name: String, age: String, email: String) {
        val profileData = ProfileData(
            name = name,
            age = age,
            email = email,
            imageUri = Uri.parse("")
        )

        viewModelScope.launch(Dispatchers.IO) {
            profileDataDao.insert(profileData)
            loadProfileList()
        }
    }

    fun removeProfile(profileData: ProfileData) {
        viewModelScope.launch(Dispatchers.IO) {
            profileDataDao.delete(profileData)
            loadProfileList()
        }
    }

    fun updateProfileData(updatedProfileData: ProfileData) {
        viewModelScope.launch(Dispatchers.IO) {
            profileDataDao.update(updatedProfileData)
            loadProfileList()
        }
    }

    fun updateProfileImage(position: Int, imageUri: Uri) {
        viewModelScope.launch(Dispatchers.IO) {
            val profileList = profileList.value.orEmpty().toMutableList()
            if (position >= 0 && position < profileList.size) {
                val updatedProfileData = profileList[position].copy(imageUri = imageUri)
                profileDataDao.update(updatedProfileData)
                loadProfileList()
            }
        }
    }
}
