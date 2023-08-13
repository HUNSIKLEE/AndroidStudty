package com.example.boardapp.data.dao

import androidx.room.*
import com.example.boardapp.data.model.ProfileData
import kotlinx.coroutines.flow.Flow

@Dao
interface ProfileDataDao {
    @Query("SELECT * FROM profile_data")
    fun getAll(): Flow<List<ProfileData>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(profileData: ProfileData)

    @Update
    suspend fun update(profileData: ProfileData)

    @Delete
    suspend fun delete(profileData: ProfileData)
}
