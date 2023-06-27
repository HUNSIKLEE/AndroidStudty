package com.example.boardapp.ui.main

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.boardapp.R
import com.example.boardapp.data.ChatMessage

class ChatAdapter(private val messageList: List<ChatMessage>) : RecyclerView.Adapter<ChatAdapter.ChatViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.chat_bubble_item, parent, false)
        return ChatViewHolder(view)
    }

    override fun onBindViewHolder(holder: ChatViewHolder, position: Int) {
        val message = messageList[position]
        holder.bind(message)
    }

    override fun getItemCount(): Int {
        return messageList.size
    }

    inner class ChatViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val messageContentTextView: TextView = itemView.findViewById(R.id.tvMessageContent)

        fun bind(message: ChatMessage) {
            messageContentTextView.text = message.content
            val backgroundRes = if (message.isUser) R.drawable.chat_bubble_user_background else R.drawable.chat_bubble_bot_background
            itemView.setBackgroundResource(backgroundRes)
        }
    }
}
