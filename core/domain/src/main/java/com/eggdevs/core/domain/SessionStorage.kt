package com.eggdevs.core.domain

import com.eggdevs.core.domain.models.AuthInfo

interface SessionStorage {
    suspend fun setInfo(authInfo: AuthInfo?)
    suspend fun getInfo(): AuthInfo?
}