package com.github.mstavares.cm.fichas

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import java.util.*

@Dao
interface OperationDao {

    @Insert
    suspend fun insert(operation: OperationRoom)

    @Insert
    suspend fun insertAll(operations: List<OperationRoom>)

    @Query("SELECT * FROM operation ORDER BY timestamp ASC")
    suspend fun getAll(): List<OperationRoom>

    @Query("SELECT * FROM operation WHERE uuid = :uuid")
    suspend fun getById(uuid: String): OperationRoom

    @Query("SELECT * FROM operation ORDER BY timestamp DESC LIMIT 1")
    suspend fun getLastOperation(): OperationRoom

    @Query("DELETE FROM operation WHERE uuid = :uuid")
    suspend fun delete(uuid: String): Int

    @Query("DELETE FROM operation")
    suspend fun deleteAll(): Int
}