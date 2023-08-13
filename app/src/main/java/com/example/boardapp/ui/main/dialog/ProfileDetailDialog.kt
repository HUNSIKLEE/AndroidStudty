// ProfileDetailDialog.kt

package com.example.boardapp.ui.main.dialog

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import androidx.activity.result.ActivityResultLauncher
import androidx.core.widget.addTextChangedListener
import com.example.boardapp.R
import com.example.boardapp.data.model.ProfileData
import com.example.boardapp.databinding.CustomDialogBinding

class ProfileDetailDialog(
    context: Context,
    private val profileData: ProfileData,
    private val onSelectImage: () -> Unit,
    private val onEditClick: (ProfileData) -> Unit,
    private val onDeleteClick: (ProfileData) -> Unit
) : Dialog(context, R.style.CustomDialog) {

    private lateinit var binding: CustomDialogBinding
    private var selectedUri: Uri? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = CustomDialogBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initView()
        setUpListeners()
    }


    private fun initView() = with(binding) {
        editName.setText(profileData.name)
        editAge.setText(profileData.age)
        editEmail.setText(profileData.email)
        dialogImage.setImageURI(profileData.imageUri)
    }


    private fun setUpListeners() {
        with(binding) {
            editName.addTextChangedListener {
                checkEnableButton()
            }

            editAge.addTextChangedListener {
                checkEnableButton()
            }

            editEmail.addTextChangedListener {
                checkEnableButton()
            }

            dialogImage.setOnClickListener {
                onSelectImage()
            }

            btnCancel.setOnClickListener {
                dismiss()
            }

            btnAdd.setOnClickListener {
                val updatedProfileData = profileData.copy(
                    name = editName.text.toString(),
                    age = editAge.text.toString(),
                    email = editEmail.text.toString(),
                    imageUri = selectedUri ?: profileData.imageUri
                )
                onEditClick(updatedProfileData)
                dismiss()
            }

            btnDelete.setOnClickListener {
                onDeleteClick(profileData)
                dismiss()
            }
        }
    }

    private fun checkEnableButton() {
        with(binding) {
            btnAdd.isEnabled =
                editName.text.isNotEmpty() && editAge.text.isNotEmpty() && editEmail.text.isNotEmpty()
        }
    }

    fun setImage(uri : Uri){
        selectedUri = uri
        binding.dialogImage.setImageURI(uri)
    }


    override fun show() {
        if (context.isFinishing()) return
        super.show()
        window?.statusBarColor = Color.parseColor("#80000000")
    }
}

fun Context.isFinishing(): Boolean = (this as? Activity)?.isFinishing == true
