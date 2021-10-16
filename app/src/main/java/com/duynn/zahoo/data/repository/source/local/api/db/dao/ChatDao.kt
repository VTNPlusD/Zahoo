package com.duynn.zahoo.data.repository.source.local.api.db.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.duynn.zahoo.data.model.ChatData
import kotlinx.coroutines.flow.Flow

/**
 * Created by duynn100198 on 10/12/21.
 */
@Dao
interface ChatDao {
    @Query("SELECT * FROM chats WHERE myId = :id and chatId = :chatId")
    fun getById(id: String, chatId: String): Flow<ChatData?>

    @Query("SELECT * FROM chats")
    fun getAll(): Flow<List<ChatData>>

    @Query("SELECT * FROM chats WHERE id <> :id")
    fun getAllNotById(id: String): Flow<List<ChatData>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(vararg chat: ChatData)

    @Delete
    suspend fun delete(chat: ChatData)

    @Query("DELETE FROM chats")
    suspend fun deleteAll()

    @Update
    suspend fun update(vararg chat: ChatData)
}
