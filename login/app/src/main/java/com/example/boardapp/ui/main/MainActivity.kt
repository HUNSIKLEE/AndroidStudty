package com.example.boardapp.ui.main

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.boardapp.data.model.ProfileData
import com.example.boardapp.databinding.ActivityMainBinding
import com.example.boardapp.ui.adapter.ProfileAdapter
import com.example.boardapp.ui.detail.ui.view.AddProfileActivity

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private val mainViewModel by viewModels<MainViewModel>()

    private val profileAdapter = ProfileAdapter(
        onItemEditClick = { profileData ->
            setItem(profileData)
        },
        onItemImageClick = { position ->
            selectedPosition = position
        },
        onItemRemoveClick = { profileData ->
            mainViewModel.removeProfile(profileData)
        }
    )

    private var selectedPosition = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initAdapter()
        initListener()
        setUpObserve()
    }

    private fun initAdapter() = with(binding) {
        rvProfile.apply {
            layoutManager = LinearLayoutManager(this@MainActivity)
            adapter = profileAdapter
        }
    }

    private fun setItem(profileData: ProfileData) {
        val intent = Intent(this, AddProfileActivity::class.java)
        intent.putExtra("name", profileData.name)
        intent.putExtra("age", profileData.age)
        intent.putExtra("email", profileData.email)
        intent.putExtra("imageUri", profileData.imageUri.toString())
        startActivity(intent)
    }

    private fun initListener() {
        binding.btnAdd.setOnClickListener {
            val intent = Intent(this, AddProfileActivity::class.java)
            startActivity(intent)
        }
    }
    private fun setUpObserve() = with(mainViewModel) {
        profileList.observe(this@MainActivity) { profileList ->
            profileAdapter.setItems(profileList)
        }
    }

    companion object {
        private const val REQ_GALLERY = 1
    }
}
