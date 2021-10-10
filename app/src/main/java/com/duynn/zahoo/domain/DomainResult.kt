package com.duynn.zahoo.domain

import com.duynn.zahoo.data.error.AppError
import com.duynn.zahoo.utils.extension.Either

/**
 *Created by duynn100198 on 10/04/21.
 */
typealias DomainResult<T> = Either<AppError, T>
