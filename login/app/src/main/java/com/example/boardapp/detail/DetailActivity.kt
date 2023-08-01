package com.example.boardapp.detail

import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import com.example.boardapp.databinding.ActivityDetailBinding
import com.example.boardapp.databinding.ActivityMainBinding
import com.example.boardapp.ui.main.MainActivity
import com.example.boardapp.ui.main.MainViewModel

class DetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailBinding

    private val detailViewModel by viewModels<MainViewModel>()

    private val imageResult = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == RESULT_OK) {


            result.data?.data?.let { uri ->
//                imageResponse(uri)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initListener()
        selectImage()

    }


    private fun initListener() = with(binding) {
        btnAdd.setOnClickListener {
            val name = editName.text.toString()
            val age = editAge.text.toString()
            val email = editEmail.text.toString()

            detailViewModel.addProfile(name, age, email)
            resetInputFields()
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
            requestPermissions(permissions, DetailActivity.REQ_GALLERY)
        }
    }


    companion object {
        private const val REQ_GALLERY = 1
    }
}