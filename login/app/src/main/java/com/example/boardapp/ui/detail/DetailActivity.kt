package com.example.boardapp.ui.detail

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.boardapp.databinding.ActivityDetailBinding

class DetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        }



    private fun setUpListener() = with(binding) {
        btnCancle.setOnClickListener {
         finish()
        }
        btnUpdate.setOnClickListener {
            
        }

    }

}
