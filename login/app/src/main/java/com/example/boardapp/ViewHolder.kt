package com.example.boardapp

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.view.menu.ActionMenuItemView
import androidx.recyclerview.widget.RecyclerView

 class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {

    private val txtName: TextView = itemView.findViewById(R.id.name_textView)
    private val txtAge: TextView = itemView.findViewById(R.id.age_textView)

    fun bind(item: ProfileData) {
        txtName.text = item.name
        txtAge.text = item.age.toString()

    }
}

