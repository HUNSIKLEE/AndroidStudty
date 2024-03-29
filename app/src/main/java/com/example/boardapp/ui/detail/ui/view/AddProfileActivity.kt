package com.example.boardapp.ui.detail.ui.view

import KeyboardVisibilityUtils
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Rect
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import com.bumptech.glide.Glide
import com.example.boardapp.R
import com.example.boardapp.data.model.ProfileData
import com.example.boardapp.databinding.ActivityAddprofileBinding


class AddProfileActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddprofileBinding

    private val addProfileViewModel by viewModels<AddProfileViewModel>()
    private lateinit var keyboardVisibilityUtils: KeyboardVisibilityUtils
    private var selectedPosition = -1
    private var selectImageUri: Uri? = null
    private var isEditMode = false


    private val imageResult = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == RESULT_OK) {
            result.data?.data?.let { uri ->
                imageResponse(uri)
                loadImage(uri)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddprofileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initListener()
        initView()

        val rootView = findViewById<View>(android.R.id.content)
        rootView.viewTreeObserver.addOnGlobalLayoutListener {
            val rect = Rect()
            rootView.getWindowVisibleDisplayFrame(rect)
            val screenHeight = rootView.height
            val keypadHeight = screenHeight - rect.bottom
        }

        keyboardVisibilityUtils = KeyboardVisibilityUtils(window,
            onShowKeyboard = { keyboardHeight ->
                binding.scrollView3.run {
                    smoothScrollTo(scrollX, scrollY + keyboardHeight)
                }
            },
            onHideKeyboard = {
                binding.scrollView3.scrollTo(0, 0)
            })
    }



    private fun initView() = with(binding) {
        if (intent.hasExtra("profile_data")) {
            isEditMode = true
            val profileData = intent.getParcelableExtra<ProfileData>("profile_data") ?: return@with

            editName.setText(profileData.name)
            editAge.setText(profileData.age)
            editEmail.setText(profileData.email)

            if (profileData.imageUri != null) {
                val uri = profileData.imageUri
                loadImage(uri)
            }

            btnAdd.text = getString(R.string.edit_button)
            btnAdd.setOnClickListener {
                val updatedName = editName.text.toString()
                val updatedAge = editAge.text.toString()
                val updatedEmail = editEmail.text.toString()
                val updatedImage = selectImageUri ?: profileData.imageUri

                val updatedProfileData = ProfileData(
                    id = profileData.id,
                    name = updatedName,
                    age = updatedAge,
                    email = updatedEmail,
                    imageUri = updatedImage
                )

                addProfileViewModel.updateProfileData(updatedProfileData)
                finish()
            }
        } else {
            initListener()
        }
    }





    private fun initListener() = with(binding) {
        btnAdd.setOnClickListener {
            val name = editName.text.toString()
            val age = editAge.text.toString()
            val email = editEmail.text.toString()

            selectImageUri?.let { it1 -> addProfileViewModel.addProfile(name, age, email, it1) }
            resetInputFields()
            finish()
        }

        imageView.setOnClickListener {
            selectImage()
        }
        btnCancel.setOnClickListener {
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
    }

    private fun resetInputFields() = with(binding) {
        editName.text.clear()
        editAge.text.clear()
        editEmail.text.clear()
    }

    private fun checkEnableButton() = with(binding) {
        val nameNotEmpty = editName.text.isNotEmpty()
        val ageNotEmpty = editAge.text.isNotEmpty()
        val emailNotEmpty = editEmail.text.isNotEmpty()

        btnAdd.isEnabled = nameNotEmpty && ageNotEmpty && emailNotEmpty
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
            requestPermissions(permissions, AddProfileActivity.REQ_GALLERY)
        }
    }

    private fun imageResponse(uri: Uri) {
        addProfileViewModel.updateProfileImage(selectedPosition, uri)
    }

    private fun loadImage(imageUri: Uri) {
        Glide.with(this)
            .load(imageUri)
            .into(binding.imageView)
        selectImageUri = imageUri
    }

    companion object {
        private const val REQ_GALLERY = 1
    }
}


