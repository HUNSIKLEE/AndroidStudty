package com.example.boardapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.boardapp.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private var mBinding:ActivityMainBinding? = null
    private val binding get() = mBinding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.loginButton.setOnClickListener {

            val id = binding.idEditTextText.text.toString()
            val pw = binding.pwEditTextTextPassword.text.toString()

            val shared = getSharedPreferences("login", MODE_PRIVATE)
            val editor = shared.edit()

            editor.putString("id", id)
            editor.putString("pw", pw)
            editor.apply()

            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)

        }
    }
}