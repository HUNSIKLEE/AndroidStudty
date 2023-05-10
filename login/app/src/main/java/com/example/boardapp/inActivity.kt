//package com.example.boardapp
//
//import android.annotation.SuppressLint
//import android.os.Bundle
//import androidx.appcompat.app.AppCompatActivity
//import androidx.recyclerview.widget.LinearLayoutManager
//import com.example.boardapp.databinding.ActivityLoginBinding
//
//class inActivity : AppCompatActivity() {
//
//    private lateinit var binding : ActivityLoginBinding
//    private val profileAdapter = ProfileAdapter()
//
//    @SuppressLint("SuspiciousIndentation")
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        binding = ActivityLoginBinding.inflate(layoutInflater)
//        setContentView(binding.root)
//
//        setUpAdapter()
//        setUpListener()
//    }
//
//    private fun setUpAdapter() = with(binding.recyclerView){
//        layoutManager = LinearLayoutManager(this@g)
//        adapter = profileAdapter
//    }
//
//    private fun setUpListener() = with(binding){
//        addButton.setOnClickListener {
//            addItem()
//        }
//    }
//
//    private fun addItem() {
//        val shared = getSharedPreferences("login", MODE_PRIVATE)
//
//        val name = binding.nameEditTextText.text.toString()
//        val age = binding.ageEditTextText.text.toString()
//        val email = binding.emailEditTextText.text.toString()
//
//        val editor = shared.edit()
//        editor.putString("name", name)
//        editor.putString("age", age)
//        editor.putString("email", email)
//        editor.apply()
//
//        profileAdapter.addItem(ProfileData(name = name, age = age, email = email))
//    }
//}