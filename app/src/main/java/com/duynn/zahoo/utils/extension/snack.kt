package com.duynn.zahoo.utils.extension

import android.view.View
import com.google.android.material.snackbar.Snackbar

/**
 *Created by duynn100198 on 10/04/21.
 */
inline fun View.snack(
    message: String,
    length: SnackBarLength = SnackBarLength.SHORT,
    crossinline f: Snackbar.() -> Unit = {}
) = Snackbar.make(this, message, length.rawValue).apply {
    f()
    show()
}

enum class SnackBarLength(val rawValue: Int) {
    SHORT(Snackbar.LENGTH_SHORT),

    LONG(Snackbar.LENGTH_LONG),

    INDEFINITE(Snackbar.LENGTH_INDEFINITE);
}
