package com.example.boardapp.ui.adapter

import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.boardapp.R
import com.example.boardapp.data.ProfileData
import com.example.boardapp.databinding.ItemProfileBinding

class ProfileAdapter(
    private val onItemClick: (Int, ProfileData) -> Unit,
    private val onImageClick: (Int) -> Unit,
    private val onRemoveClick: (ProfileData) -> Unit
) : RecyclerView.Adapter<ProfileAdapter.ViewHolder>() {

     val datas = mutableListOf<ProfileData>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemProfileBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding.root).apply {
            this.itemView.setOnClickListener {
                onItemClick(bindingAdapterPosition, datas[bindingAdapterPosition])
            }
            with(binding) {
                btnRemove.setOnClickListener {
                    val position = bindingAdapterPosition
                    if (position != RecyclerView.NO_POSITION) {
                        val profileData = datas[position]
                        onRemoveClick(profileData)
                    }

                }
                btnImage.setOnClickListener {
                    onImageClick(bindingAdapterPosition)
                }
            }
        }
    }

    override fun getItemCount(): Int = datas.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(datas[position])
    }

    fun setItems(profileList: List<ProfileData>) {
        datas.clear()
        datas.addAll(profileList)
        notifyDataSetChanged()
    }

    fun addItem(profileData: ProfileData) {
        datas.add(profileData)
        notifyItemInserted(itemCount - 1)
    }

    fun updateItem(position: Int, profileData: ProfileData) {
        datas[position] = profileData
        notifyItemChanged(position)
    }

    fun deleteItem(profileData: ProfileData ){
        val index = datas.indexOfFirst { it == profileData }
        if(index != -1){
            datas.removeAt(index)
            notifyItemRemoved(index)
        }
    }
    fun removeItem(position: Int) {
        if (position in 0 until datas.size) {
            datas.removeAt(position)
            notifyItemRemoved(position)
        }
    }

    fun updateImage(position: Int, imageUri: Uri) {
        datas[position].imageUri = imageUri
        notifyItemChanged(position)
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val binding = ItemProfileBinding.bind(itemView)

        fun bind(data: ProfileData) {
            with(binding) {
                nameTextView.text = data.name
                emailTextView.text = data.email
                ageTextView.text = data.age

                Glide.with(imageView.context)
                    .load(Uri.parse(data.imageUri.toString()))
                    .placeholder(R.drawable.baseline_account_circle_24)
                    .error(R.drawable.baseline_account_circle_24)
                    .into(imageView)
            }
        }
    }
}
