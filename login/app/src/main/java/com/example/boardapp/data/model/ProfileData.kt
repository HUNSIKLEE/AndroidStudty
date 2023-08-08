package com.example.boardapp.data.model

import android.net.Uri
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "profile_data")
data class ProfileData(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val name: String,
    val age: String,
    val email: String,
    val imageUri: Uri
): Serializable