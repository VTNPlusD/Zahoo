package com.duynn.zahoo.utils.extension

import android.view.View
import android.widget.AdapterView
import android.widget.EditText
import androidx.annotation.CheckResult
import androidx.core.widget.doOnTextChanged
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.SendChannel
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.*
import timber.log.Timber

/**
 *Created by duynn100198 on 10/04/21.
 */

fun <T> SendChannel<T>.safeOffer(element: T) = runCatching { offer(element) }.getOrDefault(false)

@ExperimentalCoroutinesApi
fun EditText.firstChange() = callbackFlow<Unit> {
    val listener = doOnTextChanged { _, _, _, _ -> safeOffer(Unit) }
    awaitClose {
        removeTextChangedListener(listener)
        Timber.d("removeTextChangedListener $listener ${this@firstChange}")
    }
}.take(1)

@ExperimentalCoroutinesApi
@CheckResult
fun EditText.textChanges() = callbackFlow<CharSequence?> {
    val listener = doOnTextChanged { text, _, _, _ ->
        safeOffer(text)
    }
    addTextChangedListener(listener)
    awaitClose { removeTextChangedListener(listener) }
}.onStart { emit(text) }

@ExperimentalCoroutinesApi
@CheckResult
fun View.clicks() = callbackFlow<View> {
    setOnClickListener { safeOffer(it) }
    awaitClose { setOnClickListener(null) }
}.conflate()

@ExperimentalCoroutinesApi
inline fun <reified T> AdapterView<*>.itemSelections() = callbackFlow<T?> {
    onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
        override fun onNothingSelected(p0: AdapterView<*>?) {
            safeOffer(null)
        }

        override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
            safeOffer(p0?.getItemAtPosition(p2) as T?)
        }
    }
    awaitClose { onItemSelectedListener = null }
}
