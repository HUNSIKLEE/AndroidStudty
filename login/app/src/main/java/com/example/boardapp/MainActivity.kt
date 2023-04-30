package com.example.boardapp

import android.app.backup.SharedPreferencesBackupHelper
import android.content.ContentValues.TAG
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.example.boardapp.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private var mBinding:ActivityMainBinding? = null
    private val binding get() = mBinding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.loginButton.setOnClickListener {
//            val id = binding.idEditTextText.text.toString()
//            val pw = binding.pwEditTextTextPassword.text.toString()

//            val intent = Intent(this, LoginActivity::class.java)
//            intent.putExtra("id", id)
//            intent.putExtra("pw", pw)
//            startActivity(intent)
//
//

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