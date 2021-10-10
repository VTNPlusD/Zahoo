package com.duynn.zahoo.utils.extension

import android.os.Looper
import android.view.View
import android.widget.AdapterView
import android.widget.EditText
import androidx.activity.OnBackPressedCallback
import androidx.activity.OnBackPressedDispatcher
import androidx.annotation.CheckResult
import androidx.appcompat.widget.SearchView
import androidx.core.widget.doOnTextChanged
import androidx.lifecycle.LifecycleOwner
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.mukesh.OnOtpCompletionListener
import com.mukesh.OtpView
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.SendChannel
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.*
import timber.log.Timber

/**
 *Created by duynn100198 on 10/04/21.
 */
internal fun checkMainThread() {
    check(Looper.myLooper() == Looper.getMainLooper()) {
        "Expected to be called on the main thread but was " + Thread.currentThread().name
    }
}

fun <T> SendChannel<T>.safeOffer(element: T) = runCatching { offer(element) }.getOrDefault(false)

@CheckResult
@OptIn(ExperimentalCoroutinesApi::class)
fun OnBackPressedDispatcher.backPresses(owner: LifecycleOwner): Flow<Unit> =
    callbackFlow<Unit> {
        checkMainThread()
        val callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                safeOffer(Unit)
            }
        }
        addCallback(owner, callback)
        awaitClose { callback.remove() }
    }.conflate()

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
@CheckResult
fun OtpView.completions() = callbackFlow<String?> {
    val listener = OnOtpCompletionListener {
        safeOffer(it)
    }
    setOtpCompletionListener(listener)
    awaitClose { setOtpCompletionListener(null) }
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

@ExperimentalCoroutinesApi
@CheckResult
fun SwipeRefreshLayout.refreshes(): Flow<Unit> {
    return callbackFlow {
        checkMainThread()
        setOnRefreshListener { safeOffer(Unit) }
        awaitClose { setOnRefreshListener(null) }
    }
}

data class SearchViewQueryTextEvent(
    val view: SearchView,
    val query: CharSequence,
    val isSubmitted: Boolean
)

@ExperimentalCoroutinesApi
@CheckResult
fun SearchView.queryTextEvents(): Flow<SearchViewQueryTextEvent> {
    return callbackFlow<SearchViewQueryTextEvent> {
        checkMainThread()

        setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                safeOffer(
                    SearchViewQueryTextEvent(
                        view = this@queryTextEvents,
                        query = query,
                        isSubmitted = true
                    )
                )
                return false
            }

            override fun onQueryTextChange(newText: String): Boolean {
                safeOffer(
                    SearchViewQueryTextEvent(
                        view = this@queryTextEvents,
                        query = newText,
                        isSubmitted = false
                    )
                )
                return true
            }
        })

        awaitClose { setOnQueryTextListener(null) }
    }.onStart {
        emit(
            SearchViewQueryTextEvent(
                view = this@queryTextEvents,
                query = query,
                isSubmitted = false
            )
        )
    }
}
