package com.duynn.zahoo.utils.extension

import android.view.View

/**
 *Created by duynn100198 on 10/04/21.
 */
fun View.visible() {
    this.visibility = View.VISIBLE
}

fun View.gone() {
    this.visibility = View.GONE
}

fun View.invisible() {
    this.visibility = View.INVISIBLE
}
