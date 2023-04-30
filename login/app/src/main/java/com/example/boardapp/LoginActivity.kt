package com.example.boardapp

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.example.boardapp.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity() {

    private var mBinding: ActivityLoginBinding? = null
    private val binding get() = mBinding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mBinding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

            val id = intent.getStringExtra("id")
            val pw = intent.getStringExtra("pw")

            val idView = binding.idTextView
            val pwView = binding.pwTextView

            idView.text = id
            pwView.text = pw
    }

}