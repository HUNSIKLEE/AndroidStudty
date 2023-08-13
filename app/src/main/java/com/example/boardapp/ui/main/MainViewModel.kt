// MainViewModel.kt

package com.example.boardapp.ui.main

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

    fun removeProfile(profileData: ProfileData) {
        viewModelScope.launch(Dispatchers.IO) {
            profileDatabase.profileDataDao().delete(profileData)
        }
    }

}
