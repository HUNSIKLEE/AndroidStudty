// MainViewModel.kt

package com.example.boardapp.ui.main

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.boardapp.data.ProfileDatabase
import com.example.boardapp.data.model.ProfileData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainViewModel : ViewModel() {

    private val profileDatabase = ProfileDatabase.instance

    private val _profileList = MutableLiveData<List<ProfileData>>()
    val profileList : LiveData<List<ProfileData>> get() = _profileList



    init {
        loadProfileList()
    }
    private fun loadProfileList() {
        viewModelScope.launch(Dispatchers.IO) {
            profileDatabase.profileDataDao().getAll().collect{
                launch(Dispatchers.Main) {
                    this@MainViewModel._profileList.value = it
                }
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
            }catch (e : IndexOutOfBoundsException){

            }
        }
    }
}
