package com.example.boardapp.data

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.boardapp.ui.main.Converters

@Database(entities = [ProfileData::class], version = 1)
@TypeConverters(Converters::class) // Add this line
abstract class ProfileDatabase : RoomDatabase() {
    abstract fun profileDataDao(): ProfileDataDao
}
