package com.example.boardapp.ui.login

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContract
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.boardapp.R
import com.example.boardapp.data.ProfileData
import com.example.boardapp.databinding.ActivityMainBinding
import com.example.boardapp.databinding.ItemProfileBinding


class ProfileAdapter : RecyclerView.Adapter<ProfileAdapter.ViewHolder>() {

    private val datas = mutableListOf<ProfileData>()
    private lateinit var binding: ActivityMainBinding


    private val activityResult: ActivityResultLauncher<Intent> = registerForActivityResult(
        ActivityResultContract.SynchronousResult()) {


        if(it.resultCode == RESULT_OK && it.data != null){

            val uri = it.data!!.data

            Glide.with(this)
                .load(uri)
                .into(binding.imageView)
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemProfileBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding.root).apply {
            with(binding) {
                btnRemove.setOnClickListener {
                    val position = bindingAdapterPosition
                    if (position != RecyclerView.NO_POSITION) {
//                        val profileData = datas[position] // 객체를 만들어서 하는것보단 포지션 번호가 훨씬 효율적이다
                        removeItem(position)
                    }
                }
                imageView.setOnClickListener {
                    val intent = Intent(Intent.ACTION_PICK)
                    intent.type = "image/*"
                    activityResult.launch(intent)

                }
            }
        }
    }

    override fun getItemCount(): Int = datas.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(datas[position])
    }

    fun addItem(profileData: ProfileData) {
        datas.add(profileData)
        notifyItemInserted(itemCount - 1)
    }

    private fun removeItem(position: Int) {
        datas.removeAt(position)
        notifyItemRemoved(position)
    }


    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val nameTextView: TextView = itemView.findViewById(R.id.name_textView)
        private val ageTextView: TextView = itemView.findViewById(R.id.age_textView)
        private val emailTextView: TextView = itemView.findViewById(R.id.email_textView)

        fun bind(data: ProfileData) {

            nameTextView.text = data.name
            emailTextView.text = data.email
            ageTextView.text = data.age.toString()


        }
    }
}
