package com.eggdevs.auth.data

import android.util.Patterns
import com.eggdevs.auth.domain.PatternValidator

object EmailValidator: PatternValidator {
    override fun matches(value: String): Boolean {
        return Patterns.EMAIL_ADDRESS.matcher(value).matches()
    }
}