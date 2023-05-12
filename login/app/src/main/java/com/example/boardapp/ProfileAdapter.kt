package com.example.boardapp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.boardapp.databinding.ActivityItemBinding
import com.example.boardapp.databinding.ActivityLoginBinding


class ProfileAdapter : RecyclerView.Adapter<ProfileAdapter.ViewHolder>() {

    private val datas = mutableListOf<ProfileData>()
    private lateinit var binding: ActivityItemBinding

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.activity_item, parent, false)
        return ViewHolder(view).apply {

        }
    }


    override fun getItemCount(): Int = datas.size

    override fun onBindViewHolder(holder: ProfileAdapter.ViewHolder, position: Int) {
        holder.bind(datas[position])
    }

    fun addItem(profileData: ProfileData){
        datas.add(profileData)
        notifyItemInserted(itemCount-1)
    }

    fun removeItem(profileData: ProfileData) {
        datas.remove(profileData)
       notifyDataSetChanged()
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