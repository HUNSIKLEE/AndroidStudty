package com.example.boardapp.ui.detail.ui.view

import android.graphics.Rect
import android.net.Uri
import android.util.Log
import android.view.View
import android.view.ViewTreeObserver
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.boardapp.data.ProfileDatabase
import com.example.boardapp.data.model.ProfileData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class AddProfileViewModel: ViewModel() {

    private val profileDatabase = ProfileDatabase.instance

    private val _profileList = MutableLiveData<List<ProfileData>>()
    private val profileList: LiveData<List<ProfileData>> get() = _profileList


    init {
        loadProfileList()
    }

    private fun loadProfileList() {
        viewModelScope.launch(Dispatchers.IO) {
            profileDatabase.profileDataDao().getAll().collect {
                launch(Dispatchers.Main) {
                    this@AddProfileViewModel._profileList.value = it
                }
            }
        }
    }

    fun addProfile(name: String, age: String, email: String, imageUri: Uri) {
        val profileData = ProfileData(
            name = name,
            age = age,
            email = email,
            imageUri = imageUri ?: Uri.parse("")
        )

        viewModelScope.launch(Dispatchers.IO) {
            profileDatabase.profileDataDao().insert(profileData)
        }
    }

    fun removeProfile(profileData: ProfileData) {
        viewModelScope.launch(Dispatchers.IO) {
            profileDatabase.profileDataDao().delete(profileData)
        }
    }

    fun updateProfileData(updatedProfileData: ProfileData) {
        viewModelScope.launch(Dispatchers.IO) {
            profileDatabase.profileDataDao().update(updatedProfileData)
        }
    }

    fun updateProfileImage(position: Int, imageUri: Uri) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                profileList.value?.let {
                    profileDatabase.profileDataDao().update(it[position].copy(imageUri = imageUri))
                }
            } catch (e: IndexOutOfBoundsException) {

            }
        }
    }





}