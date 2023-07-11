package com.example.boardapp.data.dao

import androidx.room.*
import com.example.boardapp.data.model.ProfileData

@Dao
interface ProfileDataDao {
    @Query("SELECT * FROM profile_data")
    suspend fun getAll(): List<ProfileData>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(profileData: ProfileData)

    @Update
    suspend fun update(profileData: ProfileData)

    @Delete
    suspend fun delete(profileData: ProfileData)
}
