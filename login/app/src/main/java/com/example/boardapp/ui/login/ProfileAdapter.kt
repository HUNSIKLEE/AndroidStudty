package com.example.boardapp.ui.login

import ProfileData
import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.boardapp.R
import com.example.boardapp.databinding.ActivityMainBinding
import com.example.boardapp.databinding.ItemProfileBinding
import com.example.boardapp.ui.main.OnImageClickListener

class ProfileAdapter(private val activity: Activity) : RecyclerView.Adapter<ProfileAdapter.ViewHolder>() {

    private val datas = mutableListOf<ProfileData>()
    private lateinit var binding: ActivityMainBinding

    private var imageClickListener: OnImageClickListener? = null


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemProfileBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding.root).apply {
            with(binding) {
                btnRemove.setOnClickListener {
                    val position = bindingAdapterPosition
                    if (position != RecyclerView.NO_POSITION) {
                        removeItem(position)
                    }
                }
                btnImage.setOnClickListener {
                    val position = bindingAdapterPosition
                    if(position != RecyclerView.NO_POSITION){
                        imageClickListener?.onImageClick(position)
                        val intent = Intent(Intent.ACTION_GET_CONTENT)
                        intent.type = "image/*"
                        activity.startActivityForResult(intent, 2000)
                    }
                }
            }
        }
    }
    //                        context.startActivity(intent)
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

    fun setOnImageClickListener(listener: OnImageClickListener) {
        imageClickListener = listener
    }

    fun updateImage(position: Int, imageUri: Uri) {
        datas[position].imageUri = imageUri
        notifyItemChanged(position)
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