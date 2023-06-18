package com.example.boardapp.ui.login

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.SystemClock
import android.view.KeyEvent
import android.widget.Chronometer
import android.widget.Toast
import com.example.boardapp.databinding.ActivityLoginBinding
import com.example.boardapp.ui.main.MainActivity

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding

    private var initTime = 0L
    private var pauseTimes = mutableMapOf<Chronometer, Long>()

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

            val chronometerList = listOf(chronometer, chronometer2, chronometer3)
            val startButton = btnStart
            val stopButtons = listOf(btnStop, btnStop2, btnStop3)
            val resetButtons = listOf(btnReset, btnReset2, btnReset3)

            chronometerList.forEach { chronometer ->
                pauseTimes[chronometer] = 0L
            }

            startButton.setOnClickListener {
                startChronometers(chronometerList)
                stopButtons.forEach { it.isEnabled = true }
                resetButtons.forEach { it.isEnabled = true }
            }

            stopButtons.forEachIndexed { index, stopButton ->
                stopButton.setOnClickListener {
                    stopChronometer(chronometerList[index])
                }
            }

            resetButtons.forEachIndexed { index, resetButton ->
                resetButton.setOnClickListener {
                    resetChronometer(chronometerList[index])
                }
            }
        }
    }

    private fun startChronometers(chronometers: List<Chronometer>) {
        val currentTime = SystemClock.elapsedRealtime()
        val updatedBaseTime = currentTime + pauseTimes.values.firstOrNull()!! ?: 0L

        chronometers.forEach { chronometer ->
            chronometer.base = updatedBaseTime
            chronometer.start()
        }
    }

    private fun stopChronometer(chronometer: Chronometer) {
        chronometer.stop()
        pauseTimes[chronometer] = chronometer.base - SystemClock.elapsedRealtime()
    }

    private fun resetChronometer(chronometer: Chronometer) {
        chronometer.base = SystemClock.elapsedRealtime()
        chronometer.stop()
        pauseTimes[chronometer] = 0L
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
