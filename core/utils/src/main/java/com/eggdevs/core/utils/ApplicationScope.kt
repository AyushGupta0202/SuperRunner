package com.eggdevs.core.utils

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlin.coroutines.CoroutineContext

class ApplicationScope: CoroutineScope {
    override val coroutineContext: CoroutineContext
        get() = SupervisorJob()
}