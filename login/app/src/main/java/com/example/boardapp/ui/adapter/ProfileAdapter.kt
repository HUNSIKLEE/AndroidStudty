// ProfileAdapter.kt

package com.example.boardapp.ui.adapter

import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.boardapp.R
import com.example.boardapp.data.model.ProfileData
import com.example.boardapp.databinding.ItemProfileBinding

class ProfileAdapter(
    private val onItemEditClick: (Int) -> Unit,
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

        init {
            binding.imageView.setOnClickListener {
                val position = bindingAdapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    onItemEditClick(position)
                }
            }

            binding.btnImage.setOnClickListener {
                val position = bindingAdapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    onItemImageClick(position)
                }
            }

            binding.btnRemove.setOnClickListener {
                val position = bindingAdapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    val profileData = profileList[position]
                    onItemRemoveClick(profileData)
                }
            }
        }

        fun bind(profileData: ProfileData) {
            with(binding) {
                nameTextView.text = profileData.name
                emailTextView.text = profileData.email
                ageTextView.text = profileData.age

                Glide.with(imageView.context)
                    .load(profileData.imageUri)
                    .placeholder(R.drawable.baseline_account_circle_24)
                    .error(R.drawable.baseline_account_circle_24)
                    .into(imageView)
            }
        }
    }
}
