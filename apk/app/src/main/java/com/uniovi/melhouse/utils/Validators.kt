package com.uniovi.melhouse.utils

// https://regexr.com/2rhq7
fun String.validateEmail(): Boolean {
    val regex =
        Regex("[a-z0-9!#\$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#\$%&'*+/=?^_`{|}~-]+)*@(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?")

    return regex.matches(this)
}

// https://regexr.com/3bfsi
fun String.validatePassword(): Boolean {
    val regex = Regex("^(?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[a-zA-Z]).{8,}\$")

    return regex.matches(this)
}

fun String.validateLength(min: Int = 3, max: Int = 50): Boolean {
    return this.length in min..max
}