package com.duynn.zahoo.data.repository.source.local.api.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.duynn.zahoo.data.model.ChatData
import com.duynn.zahoo.data.model.GroupData
import com.duynn.zahoo.data.model.MessageData
import com.duynn.zahoo.data.model.UserData
import com.duynn.zahoo.data.repository.source.local.api.DatabaseConfig.DATABASE_VERSION
import com.duynn.zahoo.data.repository.source.local.api.db.dao.ChatDao
import com.duynn.zahoo.data.repository.source.local.api.db.dao.GroupDao
import com.duynn.zahoo.data.repository.source.local.api.db.dao.MessageDao
import com.duynn.zahoo.data.repository.source.local.api.db.dao.UserDao

/**
 *Created by duynn100198 on 10/04/21.
 */
@TypeConverters(Converters::class)
@Database(entities = [UserData::class, GroupData::class, ChatData::class, MessageData::class],
    version = DATABASE_VERSION)
abstract class DatabaseManager : RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun groupDao(): GroupDao
    abstract fun chatDao(): ChatDao
    abstract fun messageDao(): MessageDao
}
