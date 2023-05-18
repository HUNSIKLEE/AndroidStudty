package com.example.boardapp.ui.main

import ProfileData
import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.boardapp.R
import com.example.boardapp.databinding.ActivityMainBinding
import com.example.boardapp.ui.login.LoginActivity
import com.example.boardapp.ui.login.ProfileAdapter

class MainActivity : AppCompatActivity(), ProfileAdapter.OnImageClickListener {

    private lateinit var binding: ActivityMainBinding
    private val profileAdapter = ProfileAdapter(this)


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
            val intent = Intent(this@MainActivity, LoginActivity::class.java) // @LoginActivity 써줘야 하는이유??
            startActivity(intent)
        }
    }

    fun onImageClick(position: Int) {
        // Open the photo album and handle the selected image
        val galleryLauncher = registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
            // Handle the result here
            if (uri != null) {
                // Update the image in the corresponding item of the adapter
                profileAdapter.updateImage(position, uri)
            }
        }
        galleryLauncher.launch("image/*")
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