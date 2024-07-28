package com.eggdevs.auth.domain

interface PatternValidator {
    fun matches(value: String): Boolean
}