package com.duynn.zahoo.data.repository.source.local.api.db.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.duynn.zahoo.data.model.GroupData
import kotlinx.coroutines.flow.Flow

/**
 * Created by duynn100198 on 10/12/21.
 */
@Dao
interface GroupDao {
    @Query("SELECT * FROM groups WHERE id = :id")
    fun getById(id: String): Flow<GroupData?>

    @Query("SELECT * FROM groups")
    fun getAll(): Flow<List<GroupData>>

    @Query("SELECT * FROM groups WHERE id <> :id")
    fun getAllNotById(id: String): Flow<List<GroupData>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(groups: List<GroupData>)

    @Delete
    suspend fun delete(group: GroupData)

    @Query("DELETE FROM groups")
    suspend fun deleteAll()

    @Update
    suspend fun update(vararg group: GroupData)
}
