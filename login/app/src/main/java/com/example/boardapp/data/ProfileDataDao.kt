package com.example.boardapp.data

import androidx.room.*

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
