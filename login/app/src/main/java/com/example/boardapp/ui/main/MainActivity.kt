package com.example.boardapp.ui.main

import ProfileData
import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.provider.MediaStore
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.widget.addTextChangedListener
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.boardapp.databinding.ActivityMainBinding
import com.example.boardapp.ui.login.ProfileAdapter
import com.example.boardapp.ui.main.dialog.ProfileDetailDialog


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    companion object {
        const val REQ_GALLERY = 1
    }
    private var selectedPosition = 0

    private val imageResult = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == RESULT_OK) {
            result.data?.data?.let { uri->
                if(profileDetailDialog?.isShowing == true) profileDetailDialog?.changeImage(uri)
                else profileAdapter.updateImage(selectedPosition,uri)
            }
        }
    }


    private val profileAdapter = ProfileAdapter(
        { position, profileData ->
            showDetail(position,profileData)
        },
        { position ->
            selectedImage(position)
        }
    )

    private var profileDetailDialog : ProfileDetailDialog? = null

    @SuppressLint("SuspiciousIndentation")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setUpAdapter()
        setUpListener()
        back()

    }

    private fun setUpAdapter() = with(binding.rvProfile) {
        layoutManager = LinearLayoutManager(this@MainActivity)
        adapter = profileAdapter
    }

    private fun setUpListener() = with(binding) {
        btnAdd.setOnClickListener {
            addItem()
            textReset()
            rvProfile.smoothScrollToPosition(profileAdapter.itemCount - 1)
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


    private fun textReset() = with(binding) {
        editName.text.clear()
        editAge.text.clear()
        editEmail.text.clear()
    }

    private fun checkEnableBtn() = with(binding) {
        btnAdd.isEnabled =
            editName.text.isNotEmpty() && editAge.text.isNotEmpty() && editEmail.text.isNotEmpty()
    }

    private fun back() = with(binding) {
        btnBack.setOnClickListener {
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

    private fun showDetail(position: Int, profileData: ProfileData){
        selectedPosition = position

        profileDetailDialog = ProfileDetailDialog(this, profileData, imageResult){
            profileAdapter.updateItem(selectedPosition,it)
        }

        profileDetailDialog?.show()
    }



    private fun addItem() = with(binding) {
        profileAdapter.addItem(
            ProfileData(
                name = editName.text.toString(),
                age = editAge.text.toString(),
                email = editEmail.text.toString()
            )
        )
    }

}
