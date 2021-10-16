package com.duynn.zahoo.data.repository.source.local.api.db

import com.duynn.zahoo.data.model.ChatData
import com.duynn.zahoo.data.model.GroupData
import com.duynn.zahoo.data.model.MessageData
import com.duynn.zahoo.data.model.UserData
import com.duynn.zahoo.data.repository.source.local.api.DatabaseApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged

/**
 *Created by duynn100198 on 10/04/21.
 */
class DatabaseApiImpl(private val databaseManager: DatabaseManager) : DatabaseApi {

    override fun getAllUsers(): Flow<List<UserData>> {
        return databaseManager
            .userDao()
            .getAll()
            .distinctUntilChanged()
    }

    override fun getAllUserNotById(id: String): Flow<List<UserData>> {
        return databaseManager
            .userDao()
            .getAllNotById(id)
            .distinctUntilChanged()
    }

    override suspend fun saveAllUser(users: List<UserData>) {
        return databaseManager
            .userDao()
            .insertAll(users)
    }

    override suspend fun deleteAllUser() {
        return databaseManager
            .userDao()
            .deleteAll()
    }

    override suspend fun updateUser(userData: UserData) {
        return databaseManager
            .userDao()
            .update(userData)
    }

    override fun getUserById(id: String): Flow<UserData?> {
        return databaseManager.userDao()
            .getById(id)
            .distinctUntilChanged()
    }

    override suspend fun updateGroup(groupData: GroupData) {
        return databaseManager
            .groupDao()
            .update(groupData)
    }

    override fun getGroupById(id: String): Flow<GroupData?> {
        return databaseManager.groupDao()
            .getById(id)
            .distinctUntilChanged()
    }

    override fun getChatById(id: String, chatId: String): Flow<ChatData?> {
        return databaseManager.chatDao()
            .getById(id, chatId)
            .distinctUntilChanged()
    }

    override fun getMessageById(id: String): Flow<MessageData?> {
        return databaseManager.messageDao()
            .getById(id)
            .distinctUntilChanged()
    }

    override suspend fun saveChat(chat: ChatData) {
        return databaseManager
            .chatDao()
            .insert(chat)
    }

    override suspend fun updateChat(chat: ChatData) {
        return databaseManager
            .chatDao()
            .update(chat)
    }

    override suspend fun deleteChat(chat: ChatData) {
        return databaseManager
            .chatDao()
            .delete(chat)
    }

    override suspend fun saveMessage(chat: MessageData) {
        return databaseManager
            .messageDao()
            .insert(chat)
    }

    override suspend fun updateMessage(chat: MessageData) {
        return databaseManager
            .messageDao()
            .update(chat)
    }

    override suspend fun deleteMessageById(id: String) {
        return databaseManager
            .messageDao()
            .deleteById(id)
    }

    override fun getAllMessageByChatId(id: Int): Flow<List<MessageData>> {
        return databaseManager
            .messageDao()
            .getAllByChatId(id)
    }
}
