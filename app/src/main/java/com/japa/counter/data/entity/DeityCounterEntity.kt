package com.japa.counter.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "deity_counters")
data class DeityCounterEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val name: String,
    val currentCount: Int = 0,
    val completedMalas: Int = 0,
    val isActive: Boolean = false,
    val createdAt: Long = System.currentTimeMillis()
)
