package com.github.mstavares.cm.fichas

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface OperationDao {

    @Insert
    suspend fun insert(operation: OperationRoom)

    @Query("SELECT * FROM operation ORDER BY timestamp ASC")
    suspend fun getAll(): List<OperationRoom>

    @Query("SELECT * FROM operation WHERE uuid = :uuid")
    suspend fun getById(uuid: String): OperationRoom
}