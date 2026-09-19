package com.japa.counter.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.japa.counter.data.entity.DeityCounterEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface DeityDao {

    @Query("SELECT * FROM deity_counters ORDER BY createdAt ASC")
    fun getAllDeities(): Flow<List<DeityCounterEntity>>

    @Query("SELECT * FROM deity_counters WHERE isActive = 1 LIMIT 1")
    fun getActiveDeity(): Flow<DeityCounterEntity?>

    @Query("SELECT * FROM deity_counters WHERE id = :id")
    suspend fun getDeityById(id: Long): DeityCounterEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDeity(deity: DeityCounterEntity): Long

    @Update
    suspend fun updateDeity(deity: DeityCounterEntity)

    @Delete
    suspend fun deleteDeity(deity: DeityCounterEntity)

    @Query("UPDATE deity_counters SET isActive = 0 WHERE isActive = 1")
    suspend fun clearActiveDeity()

    @Query("UPDATE deity_counters SET isActive = 1 WHERE id = :id")
    suspend fun setActiveDeity(id: Long)

    @Query("SELECT COUNT(*) FROM deity_counters")
    suspend fun getDeityCount(): Int
}
