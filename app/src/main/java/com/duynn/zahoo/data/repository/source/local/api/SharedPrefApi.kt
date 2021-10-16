package com.duynn.zahoo.data.repository.source.local.api

import com.duynn.zahoo.utils.extension.Option
import kotlinx.coroutines.flow.Flow
import kotlin.properties.ReadWriteProperty

/**
 *Created by duynn100198 on 10/04/21.
 */
interface SharedPrefApi {

    fun <T : Any?> delegate(
        defaultValue: T,
        key: String? = null,
        commit: Boolean = false
    ): ReadWriteProperty<Any, T>

    fun observeString(key: String, defValue: String? = null): Flow<Option<String>>
    fun observeStringSet(key: String, defValue: Set<String>? = null): Flow<Option<Set<String>>>
    fun observeBoolean(key: String, defValue: Boolean = false): Flow<Boolean>
    fun observeInt(key: String, defValue: Int = 0): Flow<Int>
    fun observeLong(key: String, defValue: Long = 0L): Flow<Long>
    fun observeFloat(key: String, defValue: Float = 0f): Flow<Float>
    fun <T> putList(key: String, clazz: Class<T>, list: List<T>)
    fun <T> getList(key: String, clazz: Class<T>): List<T>?
    fun removeKey(key: String)
    fun clear()
    fun <T> jsonFromList(list: List<T>?, clazz: Class<T>): String
    fun <T> jsonFormObject(data: T?, clazz: Class<T>): String
    fun <T> objectFromJson(value: String?, clazz: Class<T>): T?
    fun <T> listFromJson(value: String?, clazz: Class<T>): List<T>?
    fun <K, V> hashMapFromJson(value: String?, clazzKey: Class<K>, clazzValue: Class<V>): Map<K, V>?
    fun <K, V> jsonFromHashMap(list: Map<K, V>?, clazzKey: Class<K>, clazzValue: Class<V>): String
    fun <T> hashSetFromJson(value: String?, clazz: Class<T>): Set<T>?
    fun <T> jsonFromHashSet(list: Set<T>?, clazz: Class<T>): String
}
