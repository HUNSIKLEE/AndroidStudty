package com.example.boardapp.ui.main

import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.boardapp.data.ProfileData
import com.example.boardapp.databinding.ActivityLoginBinding
import com.example.boardapp.databinding.ActivityMainBinding
import com.example.boardapp.ui.login.ProfileAdapter


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val profileAdapter = ProfileAdapter()

    @SuppressLint("SuspiciousIndentation")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setUpAdapter()
        setUpListener()
    }

    private fun setUpAdapter() = with(binding.rvProfile) {
        layoutManager = LinearLayoutManager(this@MainActivity)
        adapter = profileAdapter
    }

    private fun setUpListener() = with(binding) {
        btnAdd.setOnClickListener {
            addItem()
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


    private fun checkEnableBtn() = with(binding) {
        btnAdd.isEnabled =
            editName.text.isNotEmpty() && editAge.text.isNotEmpty() && editEmail.text.isNotEmpty()
    }


    private fun addItem() {
        val shared = getSharedPreferences("login", MODE_PRIVATE)

        val name = binding.editName.text.toString()
        val age = binding.editAge.text.toString()
        val email = binding.editEmail.text.toString()

        val editor = shared.edit()
        editor.putString("name", name)
        editor.putString("age", age)
        editor.putString("email", email)
        editor.apply()

        profileAdapter.addItem(
            ProfileData(name = name, age = age, email = email)
        )


    }
}