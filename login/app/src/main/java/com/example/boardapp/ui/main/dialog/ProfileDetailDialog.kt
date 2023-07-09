package com.example.boardapp.ui.main.dialog

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import androidx.activity.result.ActivityResultLauncher
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.widget.addTextChangedListener
import com.bumptech.glide.Glide
import com.example.boardapp.R
import com.example.boardapp.data.ProfileData
import com.example.boardapp.databinding.CustomDialogBinding
import com.example.boardapp.ui.main.MainActivity

class ProfileDetailDialog(
    context: Context,
    private val profileData: ProfileData,
    private val imageResult : ActivityResultLauncher<Intent>,
    private val onEditClick : (ProfileData) -> Unit
) : Dialog(context, R.style.CustomDialog)  {
    private lateinit var binding: CustomDialogBinding
    private var selectedUri : Uri? = null

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = CustomDialogBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initView()
        setUpListener()
    }

    private fun initView() = with(binding){
        editName.setText(profileData.name)
        editAge.setText(profileData.age)
        editEmail.setText(profileData.email)

        Glide.with(dialogImage.context)
            .load(profileData.imageUri?:R.drawable.baseline_account_circle_24)
            .into(dialogImage)
    }

    private fun setUpListener() = with(binding){
        editName.addTextChangedListener {
            checkEnableBtn()
        }
        editAge.addTextChangedListener {
            checkEnableBtn()
        }
        editEmail.addTextChangedListener {
            checkEnableBtn()
        }

        dialogImage.setOnClickListener {
            editImage()
        }

        btnCancel.setOnClickListener {
            dismiss() }

        btnUpdate.setOnClickListener {
            onEditClick(
                profileData.copy(
                    name = editName.text?.toString()?:"",
                    age= editAge.text?.toString()?:"",
                    email = editEmail.text?.toString()?:"",
                    imageUri = selectedUri?:profileData.imageUri
                )
            )
            dismiss()
        }
        btnDelete.setOnClickListener {

        }
    }

    fun changeImage(uri : Uri) = with(binding){
        selectedUri = uri
        dialogImage.setImageURI(uri)
    }

    private fun checkEnableBtn() = with(binding) {
        btnUpdate.isEnabled =
            editName.text?.isNotEmpty()?:false && editAge.text?.isNotEmpty()?:false && editEmail.text?.isNotEmpty()?:false
    }

    private fun editImage(){
        val writePermission = ContextCompat.checkSelfPermission(
            context, Manifest.permission.WRITE_EXTERNAL_STORAGE
        )
        val readPermission = ContextCompat.checkSelfPermission(
            context, Manifest.permission.WRITE_EXTERNAL_STORAGE
        )

        if (writePermission == PackageManager.PERMISSION_DENIED || readPermission == PackageManager.PERMISSION_DENIED) {
            ActivityCompat.requestPermissions(
                (context as Activity),
                arrayOf(
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.READ_EXTERNAL_STORAGE
                ), MainActivity.REQ_GALLERY
            )
        } else {
            Intent(Intent.ACTION_PICK).apply {
                setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*")
                imageResult.launch(this)
            }
        }
    }

    override fun show() {
        if(context.isFinishing())
            return
        super.show()
        window?.statusBarColor = Color.parseColor("#80000000")
    }
}


fun Context.isFinishing() = (this as? Activity)?.isFinishing == true