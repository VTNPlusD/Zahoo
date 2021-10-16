package com.duynn.zahoo.utils.extension

import android.app.Application
import com.duynn.zahoo.utils.constants.Constants.CONFIG_ASSET_FILENAME
import com.duynn.zahoo.utils.constants.Constants.COUNTRIES_ASSET_FILENAME

/**
 * Created by duynn100198 on 10/6/21.
 */
val Application.getCountriesFromAssets
    get() = this.assets.open(COUNTRIES_ASSET_FILENAME).bufferedReader().use { it.readText() }

val Application.defaultConfigs
    get() = this.assets.open(CONFIG_ASSET_FILENAME).bufferedReader().use { it.readText() }
