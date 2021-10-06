package com.duynn.zahoo.utils.extension

import com.duynn.zahoo.data.error.AppError

/**
 *Created by duynn100198 on 10/04/21.
 */
fun AppError.getMessage(): String {
    return when (this) {
        is AppError.Remote.NetworkError -> "Network error"
        is AppError.Remote.ServerError -> errorMessage
        is AppError.Local.DatabaseError -> "Database error"
        is AppError.UnexpectedError -> "Unexpected error $errorMessage"
        AppError.LocationError.TimeoutGetCurrentLocation -> "Timeout to get current location. Please try again!"
        is AppError.LocationError.LocationSettingsDisabled -> "Location settings disabled. Please enable to continue!"
        AppError.LocationError.GeocoderEmptyResult -> "Cannot get address from coordinates"
        AppError.WrongRole -> "This app only supports doctor roles"
    }
}
