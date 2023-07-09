package com.example.boardapp.ui.main

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.widget.addTextChangedListener
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.room.Room
import com.example.boardapp.data.ProfileData
import com.example.boardapp.data.ProfileDataDao
import com.example.boardapp.data.ProfileDatabase
import com.example.boardapp.databinding.ActivityMainBinding
import com.example.boardapp.ui.adapter.ProfileAdapter
import com.example.boardapp.ui.main.dialog.ProfileDetailDialog
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var profileDatabase: ProfileDatabase
    private lateinit var profileDataDao: ProfileDataDao
    private lateinit var profileAdapter: ProfileAdapter

    companion object {
        const val REQ_GALLERY = 1
    }

    private var selectedPosition = 0

    private val imageResult = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == RESULT_OK) {
            result.data?.data?.let { uri ->
                if (profileDetailDialog?.isShowing == true) {
                    profileDetailDialog?.changeImage(uri)
                } else {
                    profileAdapter.updateImage(selectedPosition, uri)
                }
            }
        }
    }

    private var profileDetailDialog: ProfileDetailDialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        profileDatabase = Room.databaseBuilder(
            applicationContext,
            ProfileDatabase::class.java,
            "profile_database"
        ).build()
        profileDataDao = profileDatabase.profileDataDao()

        profileAdapter = ProfileAdapter(
            { position, profileData ->
                showDetail(position, profileData)
            },
            { position ->
                selectedImage(position)
            },
            { profileData ->
                removeItem(profileData)
            }
        )

        setUpAdapter()
        setUpListener()
        back()

        GlobalScope.launch(Dispatchers.IO) {
            val profileList = profileDataDao.getAll()
            withContext(Dispatchers.Main) {
                profileAdapter.setItems(profileList)
            }
        }
    }

    private fun addItem() {
        val profileData = ProfileData(
            name = binding.editName.text.toString(),
            age = binding.editAge.text.toString(),
            email = binding.editEmail.text.toString(),
            imageUri = Uri.parse("")
        )

        GlobalScope.launch(Dispatchers.IO) {
            profileDataDao.insert(profileData)
            val updatedProfileList = profileDataDao.getAll()
            withContext(Dispatchers.Main) {
                profileAdapter.setItems(updatedProfileList)
                binding.rvProfile.smoothScrollToPosition(profileAdapter.itemCount - 1)
            }
        }
    }

    private fun removeItem(profileData: ProfileData) {
        GlobalScope.launch(Dispatchers.IO) {
            profileDataDao.delete(profileData)
            val updatedProfileList = profileDataDao.getAll()
            withContext(Dispatchers.Main) {
                profileAdapter.setItems(updatedProfileList)
            }
        }
    }

    private fun setUpAdapter() {
        binding.rvProfile.apply {
            layoutManager = LinearLayoutManager(this@MainActivity)
            adapter = profileAdapter
        }
    }

    private fun setUpListener() {
        with(binding) {
            btnAdd.setOnClickListener {
                addItem()
                textReset()
            }

            editName.addTextChangedListener {
                checkEnableBtn()
            }

            editAge.addTextChangedListener {
                checkEnableBtn()
            }

            editEmail.addTextChangedListener {
                checkEnableBtn()
            }
        }
    }

    private fun textReset() {
        with(binding) {
            editName.text.clear()
            editAge.text.clear()
            editEmail.text.clear()
        }
    }

    private fun checkEnableBtn() {
        with(binding) {
            val nameNotEmpty = editName.text.isNotEmpty()
            val ageNotEmpty = editAge.text.isNotEmpty()
            val emailNotEmpty = editEmail.text.isNotEmpty()

            btnAdd.isEnabled = nameNotEmpty && ageNotEmpty && emailNotEmpty
        }
    }

    private fun back() {
        binding.btnBack.setOnClickListener {
            finish()
        }
    }

    private fun selectedImage(position: Int) {
        selectedPosition = position
        val writePermission = ContextCompat.checkSelfPermission(
            this, Manifest.permission.WRITE_EXTERNAL_STORAGE
        )
        val readPermission = ContextCompat.checkSelfPermission(
            this, Manifest.permission.WRITE_EXTERNAL_STORAGE
        )

        if (writePermission == PackageManager.PERMISSION_DENIED || readPermission == PackageManager.PERMISSION_DENIED) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.READ_EXTERNAL_STORAGE
                ), REQ_GALLERY
            )
        } else {
            Intent(Intent.ACTION_PICK).apply {
                setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*")
                imageResult.launch(this)
            }
        }
    }

    private fun showDetail(position: Int, profileData: ProfileData) {
        selectedPosition = position

        profileDetailDialog = ProfileDetailDialog(this, profileData, imageResult) { updatedProfileData ->
            profileAdapter.updateItem(selectedPosition, updatedProfileData)
        }

        profileDetailDialog?.show()
    }
}
