package com.duynn.zahoo.data.repository.source.local.api

import com.duynn.zahoo.data.model.ChatData
import com.duynn.zahoo.data.model.GroupData
import com.duynn.zahoo.data.model.MessageData
import com.duynn.zahoo.data.model.UserData
import kotlinx.coroutines.flow.Flow

/**
 *Created by duynn100198 on 10/04/21.
 */
interface DatabaseApi {
    fun getAllUsers(): Flow<List<UserData>>
    fun getAllUserNotById(id: String): Flow<List<UserData>>
    suspend fun saveAllUser(users: List<UserData>)
    suspend fun deleteAllUser()
    suspend fun updateUser(userData: UserData)
    fun getUserById(id: String): Flow<UserData?>

    suspend fun updateGroup(groupData: GroupData)
    fun getGroupById(id: String): Flow<GroupData?>

    fun getChatById(id: String, chatId: String): Flow<ChatData?>
    suspend fun saveChat(chat: ChatData)
    suspend fun updateChat(chat: ChatData)
    suspend fun deleteChat(chat: ChatData)

    suspend fun saveMessage(chat: MessageData)
    suspend fun deleteMessageById(id: String)
    suspend fun updateMessage(chat: MessageData)
    fun getMessageById(id: String): Flow<MessageData?>
    fun getAllMessageByChatId(id: Int): Flow<List<MessageData>>
}
