package com.example.boardapp.ui.main

import ProfileData
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
import com.example.boardapp.R
import com.example.boardapp.databinding.ActivityMainBinding
import com.example.boardapp.ui.login.LoginActivity
import com.example.boardapp.ui.login.ProfileAdapter


class MainActivity : AppCompatActivity(), OnImageClickListener {

    private lateinit var binding: ActivityMainBinding
    private val profileAdapter = ProfileAdapter(this)
    companion object {
        const val REQ_GALLERY = 1
    }
    private var selectedPosition = 0

    private val imageResult = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == RESULT_OK) {
            result.data?.data?.let {uri->
                profileAdapter.updateImage(selectedPosition,uri)
            }
        }
    }



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

    override fun onImageClick(position: Int) {
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



    private fun addItem() = with(binding) {
        val shared = getSharedPreferences("login", MODE_PRIVATE)

        val name = editName.text.toString()
        val age = editAge.text.toString()
        val email = editEmail.text.toString()
        val img = R.drawable.charles

        val editor = shared.edit()
        editor.putString("name", name)
        editor.putString("age", age)
        editor.putString("email", email)
        editor.putInt("img", img)
        editor.apply()

        profileAdapter.addItem(
            ProfileData(name = name, age = age, email = email, img = R.drawable.charles)
        )
    }

}
