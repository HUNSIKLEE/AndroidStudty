package com.example.boardapp.ui.main


import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.boardapp.databinding.ActivityApiBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject
import java.io.IOException
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.withContext


class ApiActivity : AppCompatActivity() {

    private lateinit var binding: ActivityApiBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityApiBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnApi.setOnClickListener {
            generateText()
        }
    }

    private fun generateText() {
        val prompt = "Please recommend only three restaurants near Seoul National University Station"
        val model = "text-davinci-002"
        val apiKey = "<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>"

        val client = OkHttpClient()

        val json = JSONObject()
        json.put("prompt", prompt)
        json.put("max_tokens", 100)
        json.put("temperature", 0.5)
        json.put("n", 1)
        json.put("stop", ".")

        val requestBody = json.toString().toRequestBody("application/json".toMediaType())

        val request = Request.Builder()
            .url("https://api.openai.com/v1/engines/$model/completions")
            .addHeader("Content-Type", "application/json")
            .addHeader("Authorization", "Bearer $apiKey")
            .post(requestBody)
            .build()

        lifecycleScope.launch(Dispatchers.IO) {
            try {
                val response = client.newCall(request).execute()
                val responseData = response.body?.string()

                withContext(Dispatchers.Main) {
                    if (response.isSuccessful && responseData != null) {
                        val data = JSONObject(responseData)
                        val generatedText = data.getJSONArray("choices")
                            .getJSONObject(0)
                            .getString("text")
                        binding.generatedTextView.text = generatedText
                    } else {
                        binding.generatedTextView.text = "Error: ${response.message}"
                    }
                }
            } catch (e: IOException) {
                withContext(Dispatchers.Main) {
                    binding.generatedTextView.text = "Error: ${e.message}"
                }
            }
        }
    }
}
