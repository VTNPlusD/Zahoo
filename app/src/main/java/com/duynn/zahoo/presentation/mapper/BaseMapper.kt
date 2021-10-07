package com.duynn.zahoo.presentation.mapper

import com.duynn.zahoo.data.model.BaseData
import com.duynn.zahoo.domain.entity.BaseEntity

/**
 *Created by duynn100198 on 10/04/21.
 */
abstract class BaseMapper<in T : BaseData, R : BaseEntity> {

    abstract fun map(data: T): R

    open fun nullableMap(entity: T?): R? {
        return entity?.let { map(it) }
    }

    open fun map(dataCollection: Collection<T>): List<R> {
        return dataCollection.map { map(it) }
    }

    open fun nullableMap(dataCollection: Collection<T>?): List<R>? {
        return dataCollection?.map { map(it) }
    }
}
