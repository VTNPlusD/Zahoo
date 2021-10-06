package com.duynn.zahoo.utils.extension

import android.app.Application

/**
 * Created by duynn100198 on 10/6/21.
 */
fun Application.getCountriesFromAssets() =
    this.assets.open("countries.json").bufferedReader().use { it.readText() }
