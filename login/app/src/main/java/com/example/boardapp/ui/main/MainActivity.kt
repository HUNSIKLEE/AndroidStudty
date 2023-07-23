// MainActivity.kt

package com.example.boardapp.ui.main

import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.room.Room
import com.example.boardapp.data.ProfileDatabase
import com.example.boardapp.databinding.ActivityMainBinding
import com.example.boardapp.ui.adapter.ProfileAdapter
import com.example.boardapp.ui.main.dialog.ProfileDetailDialog
import com.example.boardapp.ui.viewmodel.MainViewModel

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var profileDatabase: ProfileDatabase
    private lateinit var profileAdapter: ProfileAdapter
    private lateinit var mainViewModel: MainViewModel
    private val imageResult = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == RESULT_OK) {


            result.data?.data?.let { uri ->
                profileAdapter.updateImage(selectedPosition, uri)
                mainViewModel.updateProfileImage(selectedPosition, uri)
            }
        }
    }
    private var selectedPosition = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        profileDatabase = Room.databaseBuilder(
            applicationContext,
            ProfileDatabase::class.java,
            "profile_database"
        ).build()

        profileAdapter = ProfileAdapter(
            onItemEditClick = { position ->
                showProfileDetailDialog(position)
            },
            onItemImageClick = { position ->
                selectedPosition = position
                selectImage()
            },
            onItemRemoveClick = { profileData ->
                mainViewModel.removeProfile(profileData)
            }
        )

        mainViewModel = ViewModelProvider(this).get(MainViewModel::class.java)
        mainViewModel.initialize(profileDatabase.profileDataDao(), imageResult)

        with(binding) {

            rvProfile.apply {
                layoutManager = LinearLayoutManager(this@MainActivity)
                adapter = profileAdapter
            }

            btnAdd.setOnClickListener {
                val name = editName.text.toString()
                val age = editAge.text.toString()
                val email = editEmail.text.toString()

                mainViewModel.addProfile(name, age, email)
                resetInputFields()
            }

            btnBack.setOnClickListener {
                finish()
            }

            editName.addTextChangedListener {
                checkEnableButton()
            }

           editAge.addTextChangedListener {
                checkEnableButton()
            }

            editEmail.addTextChangedListener {
                checkEnableButton()
            }


            mainViewModel.profileList.observe(this@MainActivity) { profileList ->
                profileAdapter.setItems(profileList)
            }

            mainViewModel.loadProfileList()
        }
    }

    private fun resetInputFields() {
        with(binding) {
            editName.text.clear()
            editAge.text.clear()
            editEmail.text.clear()
        }
    }

    private fun checkEnableButton() {
        with(binding) {
            val nameNotEmpty = editName.text.isNotEmpty()
            val ageNotEmpty = editAge.text.isNotEmpty()
            val emailNotEmpty = editEmail.text.isNotEmpty()

            btnAdd.isEnabled = nameNotEmpty && ageNotEmpty && emailNotEmpty
        }
    }

    private fun selectImage() {
        val permissions = arrayOf(
            android.Manifest.permission.READ_EXTERNAL_STORAGE,
            android.Manifest.permission.WRITE_EXTERNAL_STORAGE
        )

        val permissionGranted = permissions.all {
            checkSelfPermission(it) == PackageManager.PERMISSION_GRANTED
        }

        if (permissionGranted) {
            val intent = Intent(Intent.ACTION_PICK)
            intent.type = "image/*"
            imageResult.launch(intent)
        } else {
            requestPermissions(permissions, REQ_GALLERY)
        }
    }
    

    private fun showProfileDetailDialog(position: Int) {
        val profileData = profileAdapter.getItem(position)
        val profileDetailDialog = ProfileDetailDialog(
            this,
            profileData,
            imageResult,
            onEditClick = { updatedProfileData ->
                mainViewModel.updateProfileData(updatedProfileData)
            },
            onDeleteClick = { updatedProfileData ->
                mainViewModel.removeProfile(updatedProfileData)
            }
        )
        profileDetailDialog.show()
    }

    companion object {
        private const val REQ_GALLERY = 1
    }
}
