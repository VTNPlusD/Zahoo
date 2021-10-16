package com.duynn.zahoo.data.repository.source.local.api.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.duynn.zahoo.data.model.MessageData
import kotlinx.coroutines.flow.Flow

/**
 * Created by duynn100198 on 10/12/21.
 */
@Dao
interface MessageDao {
    @Query("SELECT * FROM messages WHERE id = :id")
    fun getById(id: String): Flow<MessageData?>

    @Query("SELECT * FROM messages WHERE chatId = :id")
    fun getAllByChatId(id: Int): Flow<List<MessageData>>

    @Query("SELECT * FROM messages")
    fun getAll(): Flow<List<MessageData>>

    @Query("SELECT * FROM messages WHERE id <> :id")
    fun getAllNotById(id: String): Flow<List<MessageData>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(vararg message: MessageData)

    @Query("DELETE FROM messages WHERE id = :id")
    suspend fun deleteById(id: String)

    @Query("DELETE FROM messages")
    suspend fun deleteAll()

    @Update
    suspend fun update(vararg message: MessageData)
}
