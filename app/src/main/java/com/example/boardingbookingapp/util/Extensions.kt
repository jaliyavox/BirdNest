package com.example.birdnest.util

import java.text.NumberFormat
import java.util.Locale

fun String.isSliitEmail(): Boolean =
    endsWith(Constants.SLIIT_EMAIL_DOMAIN, ignoreCase = true)

fun Double.formatDistance(): String = when {
    this < 1.0 -> "${(this * 1000).toInt()} m"
    else -> String.format(Locale.getDefault(), "%.1f km", this)
}

fun Int.formatCurrency(): String =
    "Rs. ${NumberFormat.getNumberInstance(Locale.getDefault()).format(this)}"

fun haversineDistance(lat1: Double, lng1: Double, lat2: Double, lng2: Double): Double {
    val r = 6371.0
    val dLat = Math.toRadians(lat2 - lat1)
    val dLng = Math.toRadians(lng2 - lng1)
    val a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
            Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) *
            Math.sin(dLng / 2) * Math.sin(dLng / 2)
    return r * 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a))
}
