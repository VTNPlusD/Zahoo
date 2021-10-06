package com.duynn.zahoo.utils.extension

import java.util.NoSuchElementException

/**
 *Created by duynn100198 on 10/04/21.
 */
sealed class Option<out T> {

    abstract fun isEmpty(): Boolean

    data class Some<T>(val value: T) : Option<T>() {
        override fun isEmpty(): Boolean = false
    }

    object None : Option<Nothing>() {
        override fun toString() = "None"
        override fun isEmpty(): Boolean = true
    }
}

inline fun <T, R : Any> Option<T>.map(transform: (T) -> R): Option<R> = when (this) {
    is Option.Some -> Option.Some(transform(value))
    is Option.None -> Option.None
}

fun <A, B> Option<A>.mapNotNull(f: (A) -> B?): Option<B> =
    flatMap { a -> fromNullable(f(a)) }

fun <A, B> Option<A>.flatMap(f: (A) -> Option<B>): Option<B> =
    when (this) {
        is Option.None -> this
        is Option.Some -> f(value)
    }

fun <A> fromNullable(a: A?): Option<A> =
    a?.let { return@let Option.Some(it) } ?: run { return@run Option.None }

/**
 *
 */

inline fun <T, R> Option<T>.fold(ifEmpty: () -> R, ifSome: (T) -> R): R = when (this) {
    is Option.None -> ifEmpty()
    is Option.Some -> ifSome(value)
}

inline fun <T> Option<T>.getOrElse(ifNone: () -> T) = fold(ifNone) { it }

fun <T> Option<T>.getOrNull(): T? = getOrElse { null }

fun <T> Option<T>.getOrThrow(): T = getOrElse { throw NoSuchElementException("No value present") }

/**
 *
 */
fun <T> T?.toOption(): Option<T> =
    this?.let { return@let Option.Some(this) } ?: run { return@run Option.None }
