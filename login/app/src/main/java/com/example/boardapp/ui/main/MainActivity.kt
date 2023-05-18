package com.example.boardapp.ui.main

import ProfileData
import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.boardapp.R
import com.example.boardapp.databinding.ActivityMainBinding
import com.example.boardapp.ui.login.LoginActivity
import com.example.boardapp.ui.login.ProfileAdapter


class MainActivity : AppCompatActivity(), OnImageClickListener {

    private lateinit var binding: ActivityMainBinding
    private val profileAdapter = ProfileAdapter(this)
    private val INTENT_REQUEST_GET_IMAGES = 13

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
            val intent =
                Intent(this@MainActivity, LoginActivity::class.java) // @LoginActivity 써줘야 하는이유??
            startActivity(intent)
        }
    }


    override fun onImageClick(position: Int) {
        val galleryLauncher =
            registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
                // Handle the result here
                if (uri != null) {
                    // Update the image in the corresponding item of the adapter
                    profileAdapter.updateImage(position, uri)
                }
            }
        galleryLauncher.launch("image/*")
    }




//    private fun getImages() {
//        val intent = Intent(this, MainActivity::class.java)
//        startActivityForResult(intent, INTENT_REQUEST_GET_IMAGES)
//    }
//
//    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
//        super.onActivityResult(requestCode, resultCode, data)
//        if (requestCode == INTENT_REQUEST_GET_IMAGES && resultCode == RESULT_OK) {
//            val imageUris = data?.getParcelableArrayListExtra<Uri>(MainActivity.EXTRA_IMAGE_URIS)
//
//            // Do something with the image URIs
//            imageUris?.let {
//                // Process the list of image URIs
//                for (uri in it) {
//                    // Handle each URI
//                }
//            }
//        }
//    }

//-----------------------------------------------------------------------------------------------
//
//    private fun pickFromGallery() { // Pick an image from the gallery
//        val intent = Intent(Intent.ACTION_PICK)
//        intent.type = "image/*"
//        val mimeTypes = arrayOf("image/jpg", "image/jpeg", "image/png")
//        intent.putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes)
//        startActivityForResult(intent, PICK_IMAGE)
//    }
//
//    var saveName: String? = null // Name of the file to be saved
//
//    // Process the image file data selected from the gallery
//    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
//        super.onActivityResult(requestCode, resultCode, data)
//        if (requestCode == PICK_IMAGE && resultCode == RESULT_OK) {
//            if (data == null) {
//                saveName = null
//                return
//            }
//            val url = data.data
//            val filePathColumn = arrayOf(MediaStore.Images.Media.DATA)
//            val cursor = contentResolver.query(url!!, filePathColumn, null, null, null)
//            var format = ""
//            var filePath = "" // Original image format, original image path
//            var fileName = "" // Original image name
//            var file: File? = null // Original image file
//            if (cursor?.moveToFirst() == true) {
//                val columnIndex = cursor.getColumnIndex(filePathColumn[0])
//                filePath = cursor.getString(columnIndex)
//                file = File(filePath)
//                format = filePath.substring(filePath.lastIndexOf(".") + 1)
//                fileName = filePath.substring(filePath.lastIndexOf("/") + 1, filePath.lastIndexOf("."))
//                saveName = String.format("%s.%s", fileName, format) // Name of the image file to be saved
//            }
//            cursor?.close()
//
//            try {
//                // Original image bitmap
//                val bitmap = MediaStore.Images.Media.getBitmap(contentResolver, url)
//
//                // Bitmap that reflects rotation of original image
//                val rotatedBitmap = Utils.modifyOrientation(bitmap, file?.absolutePath)
//
//                // Separate GIF and image and process file saving
//                if (format.equals("gif", ignoreCase = true)) {
//                    Utils.saveGifIntoFileFromUri(applicationContext, url, saveName)
//                } else {
//                    Utils.saveImageIntoFileFromUri(applicationContext, rotatedBitmap, saveName)
//                }
//
//                // Mark the image using Glide
//                Glide.with(this)
//                    .load(url)
//                    .into(thumbnail)
//            } catch (e: FileNotFoundException) {
//                saveName = null
//                e.printStackTrace()
//            } catch (e: IOException) {
//                saveName = null
//                e.printStackTrace()
//            }
//        }
//    }
//
//
//    fun saveGifIntoFileFromUri(context: Context, uri: Uri?, fileName: String?) {
//        val openInputStream: InputStream? = context.contentResolver.openInputStream(uri!!)
//        val file: File = File(getFilePath(context), fileName)
//        try {
//            if (openInputStream != null) {
//                val inputStream: InputStream = openInputStream
//                val fileOutputStream = FileOutputStream(file)
//                inputStream.copyTo(fileOutputStream)
//                fileOutputStream.close()
//                inputStream.close()
//            }
//        } catch (e: FileNotFoundException) {
//            e.printStackTrace()
//            Log.e("Utils", "saveGifIntoFileFromUri FileNotFoundException: $e")
//        } catch (e: IOException) {
//            e.printStackTrace()
//            Log.e("Utils", "saveGifIntoFileFromUri IOException: $e")
//        }
//    }
//
//
//    fun saveImageIntoFileFromUri(context: Context?, bitmap: Bitmap, fileName: String?) {
//        val file: File = File(getFilePath(context), fileName)
//        try {
//            val fileOutputStream = FileOutputStream(file)
//            val extension = file.extension.toLowerCase()
//            when (extension) {
//                "jpeg", "jpg" -> bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fileOutputStream)
//                "png" -> bitmap.compress(Bitmap.CompressFormat.PNG, 100, fileOutputStream)
//                // Add support for other image formats if needed
//            }
//            bitmap.recycle()
//            fileOutputStream.close()
//        } catch (e: FileNotFoundException) {
//            e.printStackTrace()
//            Log.e("Utils", "saveImageIntoFileFromUri FileNotFoundException: $e")
//        } catch (e: IOException) {
//            e.printStackTrace()
//            Log.e("Utils", "saveImageIntoFileFromUri IOException: $e")
//        }
//    }
//
//
//
//    private fun getFilePath(context: Context): String? {
//        val filePath = context.filesDir.path
//        Log.e("Utils", "getFilesDir " + context.filesDir.path)
//        return filePath
//    }
//
//    @Throws(IOException::class)
//    fun modifyOrientation(bitmap: Bitmap, imageAbsolutePath: String?): Bitmap? {
//        val ei = ExifInterface(imageAbsolutePath!!)
//        return when (ei.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL)) {
//            ExifInterface.ORIENTATION_ROTATE_90 -> rotate(bitmap, 90f)
//            ExifInterface.ORIENTATION_ROTATE_180 -> rotate(bitmap, 180f)
//            ExifInterface.ORIENTATION_ROTATE_270 -> rotate(bitmap, 270f)
//            ExifInterface.ORIENTATION_FLIP_HORIZONTAL -> flip(bitmap, true, false)
//            ExifInterface.ORIENTATION_FLIP_VERTICAL -> flip(bitmap, false, true)
//            else -> bitmap
//        }
//    }
//
//    // Rotate the image
//    private fun rotate(bitmap: Bitmap, degrees: Float): Bitmap? {
//        val matrix = Matrix()
//        matrix.postRotate(degrees)
//        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.width, bitmap.height, matrix, true)
//    }
//
//    // Flip the image
//    private fun flip(bitmap: Bitmap, horizontal: Boolean, vertical: Boolean): Bitmap? {
//        val matrix = Matrix()
//        matrix.preScale(if (horizontal) -1 else 1, if (vertical) -1 else 1)
//        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.width, bitmap.height, matrix, true)
//    }


//-----------------------------------------------------------------------------------------------
//
//    val bottomSheetDialogFragment = TedBottomPicker.Builder(this@MainActivity)
//        .setOnImageSelectedListener(object : TedBottomPicker.OnImageSelectedListener {
//            override fun onImageSelected(uri: Uri) {
//
//            }
//        })
//        .create()
//
//    bottomSheetDialogFragment.show(supportFragmentManager, null)
//-----------------------------------------------------------------------------------------------
    private fun addItem() = with(binding) {
            val shared = getSharedPreferences("login", MODE_PRIVATE)

            val name = editName.text.toString()
            val age = editAge.text.toString()
            val email = editEmail.text.toString()
            val img = R.drawable.charles

            val editor = shared.edit()
            editor.putString("name", name)
            editor.putString("age", age)
            editor.putString("email", email)
            editor.putInt("img", img)
            editor.apply()

            profileAdapter.addItem(
                ProfileData(name = name, age = age, email = email, img = R.drawable.charles)
            )
        }
    }