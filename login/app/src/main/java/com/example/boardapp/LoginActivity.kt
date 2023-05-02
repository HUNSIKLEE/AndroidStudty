package com.example.boardapp

import android.graphics.drawable.Drawable
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.boardapp.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity() {

    private var mBinding: ActivityLoginBinding? = null
    private val binding get() = mBinding!!


    lateinit var profileAdapter: ProfileAdapter
    val datas = mutableListOf<ProfileData>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mBinding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
//             인텐트로 데이터 넘기기
//            val id = intent.getStringExtra("id")
//            val pw = intent.getStringExtra("pw")
//
//            val idView = binding.idTextView
//            val pwView = binding.pwTextView
//
//            idView.text = id
//            pwView.text = pw

        val shared = getSharedPreferences("login", MODE_PRIVATE)


        with(binding){
            idTextView.text =  shared.getString("id", "")
            pwTextView.text=   shared.getString("pw", "")
            addButton.setOnClickListener{

                testRecyclerView.adapter.toString()

            }
        }
    initRecycler()
    }
    private fun initRecycler() {
        profileAdapter = ProfileAdapter(this)
        binding.testRecyclerView.adapter = profileAdapter



        datas.apply {

            add(ProfileData(name = "mary", email = "test@test.com", age = 24, img = R.drawable.charles))
            add(ProfileData(name = "jenny",email = "test@test.com", age = 26, img = R.drawable.charles))
            add(ProfileData(name = "jhon", email = "test@test.com",age = 27, img = R.drawable.charles))
            add(ProfileData(name = "ruby", email = "test@test.com", age = 21, img = R.drawable.charles))
            add(ProfileData(name = "yuna", email = "test@test.com",age = 23, img = R.drawable.charles))

            profileAdapter.datas = datas
            profileAdapter.notifyDataSetChanged()
        }
    }


}