package com.example.boardapp

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.boardapp.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity() {


    private var mBinding: ActivityLoginBinding? = null
    private val binding get() = mBinding!!
    lateinit var profileAdapter: ProfileAdapter
    val datas = mutableListOf<ProfileData>()

    @SuppressLint("SuspiciousIndentation")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.recyclerView.layoutManager = LinearLayoutManager(this)

        val shared = getSharedPreferences("login", MODE_PRIVATE)

        profileAdapter = ProfileAdapter(datas)

        with(binding) {

            recyclerView.adapter = profileAdapter

            addButton.setOnClickListener {
                initRecycler()
            }
        }
    }

    private fun initRecycler() {
        val shared = getSharedPreferences("login", MODE_PRIVATE)

        val name = binding.nameEditTextText.text.toString()
        val age = binding.ageEditTextText.text.toString()
        val email = binding.emailEditTextText.text.toString()

        val editor = shared.edit()
        editor.putString("name", name)
        editor.putString("age", age)
        editor.putString("email", email)
        editor.apply()


            val newData = ProfileData(name = name, age = age, email = email)
            datas.add(newData)
            profileAdapter.notifyItemInserted(datas.size - 1)
            profileAdapter.notifyDataSetChanged() // add this line to update the adapter
        }
    }
