package com.japa.counter.data.repository

import com.japa.counter.data.dao.DeityDao
import com.japa.counter.data.entity.DeityCounterEntity
import kotlinx.coroutines.flow.Flow

class DeityRepository(private val deityDao: DeityDao) {

    val allDeities: Flow<List<DeityCounterEntity>> = deityDao.getAllDeities()
    val activeDeity: Flow<DeityCounterEntity?> = deityDao.getActiveDeity()

    suspend fun getDeityById(id: Long): DeityCounterEntity? = deityDao.getDeityById(id)

    suspend fun insertDeity(deity: DeityCounterEntity): Long = deityDao.insertDeity(deity)

    suspend fun updateDeity(deity: DeityCounterEntity) = deityDao.updateDeity(deity)

    suspend fun deleteDeity(deity: DeityCounterEntity) = deityDao.deleteDeity(deity)

    suspend fun setActiveDeity(id: Long) {
        deityDao.clearActiveDeity()
        deityDao.setActiveDeity(id)
    }
}
