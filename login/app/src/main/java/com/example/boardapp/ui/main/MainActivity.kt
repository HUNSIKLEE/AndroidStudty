package com.example.boardapp.ui.main

import ProfileData
import android.Manifest
import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.widget.addTextChangedListener
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.boardapp.R
import com.example.boardapp.databinding.ActivityMainBinding
import com.example.boardapp.ui.login.ProfileAdapter

class MainActivity : AppCompatActivity(), OnImageClickListener {

    private lateinit var binding: ActivityMainBinding
    private val profileAdapter = ProfileAdapter(this)

    companion object {
        const val REQ_GALLERY = 1
    }

    private var selectedPosition = 0

    private val imageResult = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == RESULT_OK) {
            result.data?.data?.let { uri ->
                profileAdapter.updateImage(selectedPosition, uri)
            }
        }
    }


    @SuppressLint("SuspiciousIndentation")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setUpAdapter()
        setUpListener()
        back()

    }

    private fun setUpAdapter() = with(binding.rvProfile) {
        layoutManager = LinearLayoutManager(this@MainActivity)
        adapter = profileAdapter
    }

    private fun setUpListener() = with(binding) {
        btnAdd.setOnClickListener {
            addItem()
            textReset()
            rvProfile.scrollToPosition(1);
        }

        editName.addTextChangedListener {
            checkEnableBtn()
        }

        editAge.addTextChangedListener {
            checkEnableBtn()
        }
        editEmail.addTextChangedListener {
            checkEnableBtn()
        }

    }


    private fun textReset() = with(binding) {
        editName.text.clear()
        editAge.text.clear()
        editEmail.text.clear()
    }

    private fun checkEnableBtn() = with(binding) {
        btnAdd.isEnabled =
            editName.text.isNotEmpty() && editAge.text.isNotEmpty() && editEmail.text.isNotEmpty()

    }

    private fun back() = with(binding) {
        btnBack.setOnClickListener {
            finish()
        }
    }

    override fun onImageClick(position: Int) {
        selectedPosition = position
        val writePermission = ContextCompat.checkSelfPermission(
            this, Manifest.permission.WRITE_EXTERNAL_STORAGE
        )
        val readPermission = ContextCompat.checkSelfPermission(
            this, Manifest.permission.WRITE_EXTERNAL_STORAGE
        )

        if (writePermission == PackageManager.PERMISSION_DENIED || readPermission == PackageManager.PERMISSION_DENIED) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.READ_EXTERNAL_STORAGE
                ), REQ_GALLERY
            )
        } else {
            Intent(Intent.ACTION_PICK).apply {
                setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*")
                imageResult.launch(this)
            }
        }
    }

    override fun onImageViewClick(position: Int) {
        OnClick(position)
    }


    private fun addItem() = with(binding) {
        val shared = getSharedPreferences("login", MODE_PRIVATE)

        val name = editName.text.toString()
        val age = editAge.text.toString()
        val email = editEmail.text.toString()
        val img = R.drawable.charles
        val imageUri = null // Provide a valid image URI here

        val editor = shared.edit()
        editor.putString("name", name)
        editor.putString("age", age)
        editor.putString("email", email)
        editor.putInt("img", img)
        editor.apply()

        profileAdapter.addItem(
            ProfileData(name = name, age = age, email = email, imageUri = imageUri, img = img)
        )
    }

    private fun OnClick(position: Int) {
        val dialog = Dialog(this@MainActivity)
        dialog.setContentView(R.layout.custom_dialog)

        val updateBtn = dialog.findViewById<Button>(R.id.btn_update)
        val cancelBtn = dialog.findViewById<Button>(R.id.btn_cancel)
        val deleteBtn = dialog.findViewById<Button>(R.id.btn_delete)
        val imgView = dialog.findViewById<ImageView>(R.id.dialog_image)
        val nameTextView = dialog.findViewById<TextView>(R.id.edit_name)
        val ageTextView = dialog.findViewById<TextView>(R.id.edit_age)
        val emailTextView = dialog.findViewById<TextView>(R.id.edit_email)

        val shared = getSharedPreferences("login", MODE_PRIVATE)
        val name = shared.getString("name", "")
        val age = shared.getString("age", "")
        val email = shared.getString("email", "")
        val img = shared.getInt("img", 0)
        val imageUri = null // Provide a valid image URI here

        nameTextView.text = name
        ageTextView.text = age
        emailTextView.text = email
        imgView.setImageResource(img)

        updateBtn.setOnClickListener {
            // Get the updated values from the EditText fields
            val updatedName = nameTextView.text.toString()
            val updatedAge = ageTextView.text.toString()
            val updatedEmail = emailTextView.text.toString()

            // Update the data in SharedPreferences
            val editor = shared.edit()
            editor.putString("name", updatedName)
            editor.putString("age", updatedAge)
            editor.putString("email", updatedEmail)
            editor.apply()

            // Update the data in the RecyclerView adapter
            val updatedProfileData = ProfileData(updatedName, updatedAge, updatedEmail, imageUri, img)
            profileAdapter.updateItem(position, updatedProfileData)

            dialog.dismiss()
        }

        cancelBtn.setOnClickListener {
            dialog.dismiss()
        }

        deleteBtn.setOnClickListener {
            profileAdapter.removeItem(position)
            dialog.dismiss()
        }

        imgView.setOnClickListener {
            onImageClick(position)
            dialog.dismiss()
        }

        dialog.show()
    }
}
