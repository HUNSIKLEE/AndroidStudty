package com.example.boardapp.ui.main


import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.boardapp.data.ChatMessage
import com.example.boardapp.databinding.ActivityApiBinding
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import org.json.JSONException
import org.json.JSONObject
import java.io.IOException

class ApiActivity : AppCompatActivity() {

    private lateinit var binding: ActivityApiBinding
    private val client = OkHttpClient()
    private val messageList: MutableList<ChatMessage> = mutableListOf()
    private lateinit var chatAdapter: ChatAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityApiBinding.inflate(layoutInflater)
        setContentView(binding.root)

        chatAdapter = ChatAdapter(messageList)
        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        binding.recyclerView.adapter = chatAdapter

        binding.btnSend.setOnClickListener {
            val userInput = binding.etMsg.text.toString().trim()
            if (userInput.isNotEmpty()) {
                addToChat(userInput, isUser = true)
                binding.etMsg.text.clear()
                requestChatAPI(userInput)
            }
        }
    }

    private fun addToChat(message: String, isUser: Boolean) {
        val chatMessage = ChatMessage(message, isUser)
        messageList.add(chatMessage)
        chatAdapter.notifyItemInserted(messageList.size - 1)
        binding.recyclerView.scrollToPosition(messageList.size - 1)
    }

    private fun requestChatAPI(userInput: String) {
        val jsonObject = JSONObject()
        try {
            jsonObject.put("messages", createMessagePayload(userInput))
        } catch (e: JSONException) {
            e.printStackTrace()
        }
        val requestBody = RequestBody.create("application/json".toMediaTypeOrNull(), jsonObject.toString())
        val request = Request.Builder()
            .url("https://api.openai.com/v1/chat/completions")
            .addHeader("Authorization", "Bearer YOUR_API_KEY")
            .post(requestBody)
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                e.printStackTrace()
            }

            @Throws(IOException::class)
            override fun onResponse(call: Call, response: Response) {
                if (response.isSuccessful) {
                    val responseBody = response.body?.string()
                    processChatAPIResponse(responseBody)
                } else {
                    // Handle API error
                }
            }
        })
    }

    private fun createMessagePayload(userInput: String): List<JSONObject> {
        val userMessage = JSONObject()
        userMessage.put("role", "system")
        userMessage.put("content", userInput)

        return listOf(userMessage)
    }

    private fun processChatAPIResponse(responseBody: String?) {
        val responseJson = JSONObject(responseBody)
        val choicesArray = responseJson.getJSONArray("choices")
        if (choicesArray.length() > 0) {
            val generatedMessage = choicesArray.getJSONObject(0).getString("message")
            addToChat(generatedMessage, isUser = false)
        }
    }
}
