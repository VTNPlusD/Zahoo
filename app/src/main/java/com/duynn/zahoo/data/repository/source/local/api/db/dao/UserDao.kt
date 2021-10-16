package com.duynn.zahoo.data.repository.source.local.api.db.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.duynn.zahoo.data.model.UserData
import kotlinx.coroutines.flow.Flow

/**
 *Created by duynn100198 on 10/04/21.
 */
@Dao
interface UserDao {
    @Query("SELECT * FROM users WHERE id = :id")
    fun getById(id: String): Flow<UserData?>

    @Query("SELECT * FROM users")
    fun getAll(): Flow<List<UserData>>

    @Query("SELECT * FROM users WHERE id <> :id")
    fun getAllNotById(id: String): Flow<List<UserData>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(users: List<UserData>)

    @Delete
    suspend fun delete(user: UserData)

    @Query("DELETE FROM users")
    suspend fun deleteAll()

    @Update
    suspend fun update(vararg user: UserData)
}
