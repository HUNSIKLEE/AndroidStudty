package com.example.boardapp.api.ui.view

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.boardapp.api.data.Coin
import com.example.boardapp.databinding.ActivityApiBinding
import com.example.boardapp.ui.adapter.ListAdapter
import com.example.boardapp.api.data.ApiObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ApiActivity  : AppCompatActivity() {
    private val binding: ActivityApiBinding by lazy { ActivityApiBinding.inflate(layoutInflater) }
    lateinit var listAdapter: ListAdapter
    var coinList = listOf<Coin>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        listAdapter = ListAdapter()

        binding.btn01.setOnClickListener {
            initList()
        }

        binding.recycler01.apply {
            adapter = listAdapter
            layoutManager = LinearLayoutManager(context)
            setHasFixedSize(true)
        }
    }

    private fun initList() {
        val call = ApiObject.getRetrofitService.getCoinAll()
        call.enqueue(object: Callback<List<Coin>> {
            override fun onResponse(call: Call<List<Coin>>, response: Response<List<Coin>>) {
                Toast.makeText(applicationContext, "Call Success", Toast.LENGTH_SHORT).show()
                if(response.isSuccessful) {
                    coinList = response.body() ?: listOf()
                    listAdapter.setList(coinList)
                    listAdapter.notifyDataSetChanged()
                }
            }

            override fun onFailure(call: Call<List<Coin>>, t: Throwable) {
                Toast.makeText(applicationContext, "Call Failed", Toast.LENGTH_SHORT).show()
            }
        })
    }

}