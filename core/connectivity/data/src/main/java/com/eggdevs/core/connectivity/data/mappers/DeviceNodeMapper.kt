package com.eggdevs.core.connectivity.data.mappers

import com.eggdevs.core.connectivity.domain.DeviceNode
import com.google.android.gms.wearable.Node

fun Node.toDeviceNode(): DeviceNode {
    return DeviceNode(
        id = id,
        displayName = displayName,
        isNearby = isNearby
    )
}