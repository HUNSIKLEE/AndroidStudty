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
        var startTime = 0L
        val handler = Handler()
        lateinit var runnable: Runnable

        fun updateTextView() {
            val elapsedTime = SystemClock.elapsedRealtime() - startTime
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
                startTime = SystemClock.elapsedRealtime()
                updateTextView()
                isRunning = true

                runnable = object : Runnable {
                    override fun run() {
                        updateTextView()
                        handler.postDelayed(this, 1000) // Update every 1 second
                    }
                }
                handler.postDelayed(runnable, 1000)
            } else {
                isRunning = false
                handler.removeCallbacks(runnable)
            }
            toggleButtons()
        }


        // Initial button state
        toggleButtons()
    }
}
