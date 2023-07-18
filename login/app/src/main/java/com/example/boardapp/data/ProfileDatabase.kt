package com.example.boardapp.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.boardapp.data.dao.ProfileDataDao
import com.example.boardapp.data.model.ProfileData
import com.example.boardapp.utils.Converters

@Database(entities = [ProfileData::class], version = 1, exportSchema = false)
@TypeConverters(Converters::class) // Add this line
abstract class ProfileDatabase : RoomDatabase() {
    abstract fun profileDataDao(): ProfileDataDao
    companion object{
        lateinit var instance: ProfileDatabase

        @Synchronized
        fun setInstance(context: Context) {
            if (!::instance.isInitialized) {
                instance = Room.databaseBuilder(
                    context.applicationContext,
                    ProfileDatabase::class.java,
                    "profile_database"
                ).build()
            }
        }
    }
}
