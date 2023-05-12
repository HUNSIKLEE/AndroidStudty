package com.example.boardapp

import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.boardapp.databinding.ActivityLoginBinding


class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private val profileAdapter = ProfileAdapter()

    @SuppressLint("SuspiciousIndentation")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setUpAdapter()
        setUpListener()
    }

    private fun setUpAdapter() = with(binding.rvProfile) {
        layoutManager = LinearLayoutManager(this@LoginActivity)
        adapter = profileAdapter
    }

    private fun setUpListener() = with(binding) {
        btnAdd.setOnClickListener {
            addItem()
        }

    }

    private fun addItem() {
        val shared = getSharedPreferences("login", MODE_PRIVATE)

        val name = binding.editName.text.toString()
        val age = binding.editAge.text.toString()
        val email = binding.editEmail.text.toString()


        if (name.isBlank() || age.isBlank() || email.isBlank()) {
            Toast.makeText(this, "빈칸을 모두 입력해주세요.", Toast.LENGTH_SHORT).show()
            return
        }

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