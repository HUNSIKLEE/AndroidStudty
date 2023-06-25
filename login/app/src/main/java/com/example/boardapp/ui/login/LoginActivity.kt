package com.example.boardapp.ui.login

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.SystemClock
import android.view.KeyEvent
import android.widget.Button
import android.widget.Chronometer
import android.widget.TextView
import android.widget.Toast
import com.example.boardapp.databinding.ActivityLoginBinding
import com.example.boardapp.ui.main.MainActivity
import java.util.Timer
import kotlin.concurrent.timerTask

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding

    private var initTime = 0L // 뒤로 가기 버튼을 누른 시각을 저장하는 속성
    private var pauseTime = 0L // 멈춘 시각을 저장하는 속성

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        with(binding) {
            loginButton.setOnClickListener {
                val id = idEditTextText.text.toString()
                val pw = pwEditTextTextPassword.text.toString()

                val shared = getSharedPreferences("login", MODE_PRIVATE)
                val editor = shared.edit()

                editor.putString("id", id)
                editor.putString("pw", pw)
                editor.apply()

                val intent = Intent(this@LoginActivity, MainActivity::class.java)
                startActivity(intent)
            }

            setupTimer(tvTimer, btnStart )
            setupTimer(tvTimer2, btnStart2)
            setupTimer(tvTimer3, btnStart3)
        }
    }

    private fun setupTimer(
        textView: TextView,
        startButton: Button,
    ) {
        var isRunning = false
        var elapsedTime = 0L
        var timer: Timer? = null

        fun updateTextView() {
            val seconds = (elapsedTime / 1000).toInt()
            val minutes = seconds / 60
            val remainingSeconds = seconds % 60
            val timeFormatted = String.format("%02d:%02d", minutes, remainingSeconds)
            textView.text = timeFormatted
        }

        fun toggleButtons() {
            if (isRunning) {
                startButton.text = "Stop"
            } else {
                startButton.text = "Start"
            }
        }

        startButton.setOnClickListener {
            if (!isRunning) {
                elapsedTime = 0L
                updateTextView()
                isRunning = true

                timer = Timer()
                timer?.scheduleAtFixedRate(timerTask {
                    elapsedTime += 1000
                    runOnUiThread {
                        updateTextView()
                    }
                }, 1000, 1000)
            } else {
                isRunning = false
                timer?.cancel()
                timer = null
            }
            toggleButtons()
        }

        // Initial button state
        toggleButtons()
    }

}
