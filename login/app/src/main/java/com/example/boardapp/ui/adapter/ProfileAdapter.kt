// ProfileAdapter.kt

package com.example.boardapp.ui.adapter

import android.annotation.SuppressLint
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.boardapp.R
import com.example.boardapp.data.model.ProfileData
import com.example.boardapp.databinding.ItemProfileBinding

class ProfileAdapter(
    private val onItemEditClick: (ProfileData) -> Unit,
    private val onItemImageClick: (Int) -> Unit,
    private val onItemRemoveClick: (ProfileData) -> Unit
) : RecyclerView.Adapter<ProfileAdapter.ViewHolder>() {

    private val profileList = mutableListOf<ProfileData>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemProfileBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(profileList[position])
    }

    override fun getItemCount(): Int = profileList.size

    @SuppressLint("NotifyDataSetChanged")
    fun setItems(items: List<ProfileData>) {
        profileList.clear()
        profileList.addAll(items)
        notifyDataSetChanged()
    }

    fun getItem(position: Int): ProfileData {
        return profileList[position]
    }

    fun updateImage(position: Int, imageUri: Uri) {
        val profileData = profileList[position].copy(imageUri = imageUri)
        profileList[position] = profileData
        notifyItemChanged(position)
    }

    inner class ViewHolder(private val binding: ItemProfileBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(profileData: ProfileData) {
            with(binding){
                item = profileData

                itemProfile.setOnClickListener {
                    onItemEditClick(profileData)
                }

                btnImage.setOnClickListener {
                    val position = bindingAdapterPosition
                    if (position != RecyclerView.NO_POSITION) {
                        onItemImageClick(position)
                    }
                }

                btnRemove.setOnClickListener {
                    onItemRemoveClick(profileData)
                }
            }
        }
    }
}
