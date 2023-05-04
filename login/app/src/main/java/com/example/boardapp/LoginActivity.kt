package com.example.boardapp

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.boardapp.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity() {

    private var mBinding: ActivityLoginBinding? = null
    private val binding get() = mBinding!!


    lateinit var profileAdapter: ProfileAdapter
    val datas = mutableListOf<ProfileData>()

    lateinit var editText: EditText
    lateinit var btnadd: Button
    lateinit var rvText: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mBinding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)


        val shared = getSharedPreferences("login", MODE_PRIVATE)


        with(binding) {

            nameTextView.text = shared.getString("id", "")
            pwTextView.text = shared.getString("pw", "")

            addButton.setOnClickListener {


                initRecycler()

            }
        }

    }

    private fun initRecycler() {
        profileAdapter = ProfileAdapter(this)
        binding.recyclerView.adapter = profileAdapter


//
//        datas.apply {
//
//            add(
//                ProfileData(
//                    name = "charlse",
//                    email = "test@test.com",
//                    age = 4,
//                    img = R.drawable.charles
//                )
//            )
//            add(
//                ProfileData(
//                    name = "sik",
//                    email = "test@test.com",
//                    age = 34,
//                    img = R.drawable.ic_launcher_background
//                )
//            )
//            add(
//                ProfileData(
//                    name = "sung",
//                    email = "test@test.com",
//                    age = 35,
//                    img = R.drawable.ic_launcher_foreground
//                )
//            )
//            add(
//                ProfileData(
//                    name = "segi",
//                    email = "test@test.com",
//                    age = 34,
//                    img = R.drawable.charles
//                )
//            )
//            add(
//                ProfileData(
//                    name = "young",
//                    email = "test@test.com",
//                    age = 31,
//                    img = R.drawable.charles
//                )
//            )
        val newData = ProfileData(
            name = "New Name",
            email = "new@test.com",
            age = 25,
            img = R.drawable.charles
        )

        datas.add(newData)
        profileAdapter.datas = datas
        profileAdapter.notifyItemInserted(datas.size - 1)
//            profileAdapter.datas = datas
//            profileAdapter.notifyItemRangeInserted(datas.size - 1)
        }
    }

