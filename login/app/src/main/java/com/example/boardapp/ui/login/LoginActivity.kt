package com.example.boardapp.ui.login

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.SystemClock
import android.view.KeyEvent
import android.widget.Button
import android.widget.Chronometer
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

            setupChronometer(chronometer, btnStart, btnStop, btnReset)
            setupChronometer(chronometer2, btnStart2, btnStop2, btnReset2)
            setupChronometer(chronometer3, btnStart3, btnStop3, btnReset3)
        }
    }

    private fun setupChronometer(
        chronometer: Chronometer,
        startButton: Button,
        stopButton: Button,
        resetButton: Button
    ) {
        startButton.setOnClickListener {
            val currentTime = SystemClock.elapsedRealtime()
            val updatedBaseTime = currentTime + pauseTime

            chronometer.base = updatedBaseTime
            chronometer.start()

            // Button visibility adjustment
            startButton.isEnabled = false
            stopButton.isEnabled = true
            resetButton.isEnabled = true
        }

        stopButton.setOnClickListener {
            pauseTime = chronometer.base - SystemClock.elapsedRealtime()
            chronometer.stop()

            // Button visibility adjustment
            startButton.isEnabled = true
            stopButton.isEnabled = false
            resetButton.isEnabled = true
        }

        resetButton.setOnClickListener {
            pauseTime = 0L
            chronometer.base = SystemClock.elapsedRealtime()
            chronometer.stop()

            // Button visibility adjustment
            startButton.isEnabled = true
            stopButton.isEnabled = false
            resetButton.isEnabled = false
        }
    }

    // 뒤로 가기 버튼 이벤트 핸들러
    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (System.currentTimeMillis() - initTime > 3000) {
                Toast.makeText(this, "종료하려면 한 번 더 누르세요!!", Toast.LENGTH_SHORT).show()
                initTime = System.currentTimeMillis()
                return true
            }
        }
        return super.onKeyDown(keyCode, event)
    }
}
