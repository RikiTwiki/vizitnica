package com.walcanty.businesscard.data

import androidx.lifecycle.LiveData
import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface BusinessCardDao {

//    @Query("SELECT * FROM BUSINESSCARD ORDER BY name ASC")
//    fun getAll():LiveData<List<BusinessCard>>

    @Query("SELECT * FROM BUSINESSCARD ORDER BY name ASC")
    fun getAll():Flow<List<BusinessCard>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(businessCard: BusinessCard)

//    @Query("DELETE FROM BUSINESSCARD WHERE id = :id")
//    suspend fun delete(id: Int)

    @Update
    suspend fun update(businessCard: BusinessCard)

    @Delete
    suspend fun delete(businessCard: BusinessCard)

    @Query("SELECT * FROM BUSINESSCARD WHERE id = :id")
    suspend fun getCardById(id: Int): BusinessCard
}