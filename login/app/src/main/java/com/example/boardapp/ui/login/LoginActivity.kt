package com.example.boardapp.ui.login

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.boardapp.databinding.ActivityLoginBinding
import com.example.boardapp.ui.main.MainActivity
import com.example.boardapp.databinding.ActivityMainBinding

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        with(binding) {
            loginButton.setOnClickListener {

                val id = idEditTextText.text.toString()
                val pw = pwEditTextTextPassword.text.toString()

                val shared = getSharedPreferences("login", MODE_PRIVATE)
                val editor = shared.edit()

                editor.putString("id", id)
                editor.putString("pw", pw)
                editor.apply()

                val intent = Intent(this@LoginActivity, MainActivity::class.java) // @LoginActivity 써줘야 하는이유??
                startActivity(intent)
            }
        }
    }
}