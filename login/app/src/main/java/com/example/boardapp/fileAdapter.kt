//package com.example.boardapp
//
//import android.view.LayoutInflater
//import android.view.View
//import android.view.ViewGroup
//import android.widget.TextView
//import androidx.recyclerview.widget.RecyclerView
//
//
//class fileAdapter : RecyclerView.Adapter<ProfileAdapter.ViewHolder>() {
//
//    private val datas = mutableListOf<ProfileData>()
//
//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
//        val view = LayoutInflater.from(parent.context).inflate(R.layout.activity_item, parent, false)
//        return ViewHolder(view).apply {
//
//        }
//    }
//
//    override fun getItemCount(): Int = datas.size
//
//    override fun onBindViewHolder(holder: ProfileAdapter.ViewHolder, position: Int) {
//        holder.bind(datas[position])
//    }
//
//    fun addItem(profileData: ProfileData){
//        datas.add(profileData)
//        notifyItemInserted(itemCount-1)
//    }
//
//    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
//        private val nameTextView: TextView = itemView.findViewById(R.id.name_textView)
//        private val ageTextView: TextView = itemView.findViewById(R.id.age_textView)
//        private val emailTextView: TextView = itemView.findViewById(R.id.email_textView)
//        fun bind(data: ProfileData) {
//
//            nameTextView.text = data.name
//            emailTextView.text = data.email
//            ageTextView.text = data.age.toString()
//
//        }
//    }
//}