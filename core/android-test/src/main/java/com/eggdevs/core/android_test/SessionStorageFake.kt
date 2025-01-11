package com.eggdevs.core.android_test

import com.eggdevs.core.domain.SessionStorage
import com.eggdevs.core.domain.models.AuthInfo

class SessionStorageFake: SessionStorage {

    private var authInfo: AuthInfo? = null

    override suspend fun setInfo(authInfo: AuthInfo?) {
        this.authInfo = authInfo
    }

    override suspend fun getInfo(): AuthInfo? {
        return authInfo
    }
}