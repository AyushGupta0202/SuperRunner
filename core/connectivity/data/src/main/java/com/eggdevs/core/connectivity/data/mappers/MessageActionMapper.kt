package com.eggdevs.core.connectivity.data.mappers

import com.eggdevs.core.connectivity.data.messaging.MessagingActionDto
import com.eggdevs.core.connectivity.messaging.MessagingAction

fun MessagingAction.toMessagingActionDto(): MessagingActionDto {
    return when(this) {
        MessagingAction.ConnectionRequest -> MessagingActionDto.ConnectionRequest
        is MessagingAction.DistanceUpdate -> MessagingActionDto.DistanceUpdate(distanceMeters)
        MessagingAction.Finish -> MessagingActionDto.Finish
        is MessagingAction.HeartRateUpdate -> MessagingActionDto.HeartRateUpdate(heartRate)
        MessagingAction.Pause -> MessagingActionDto.Pause
        MessagingAction.StartOrResume -> MessagingActionDto.StartOrResume
        is MessagingAction.TimeUpdate -> MessagingActionDto.TimeUpdate(elapsedDuration)
        MessagingAction.Trackable -> MessagingActionDto.Trackable
        MessagingAction.Untrackable -> MessagingActionDto.Untrackable
        MessagingAction.CancelRun -> MessagingActionDto.CancelRun
    }
}

fun MessagingActionDto.toMessagingAction(): MessagingAction {
    return when(this) {
        MessagingActionDto.ConnectionRequest -> MessagingAction.ConnectionRequest
        is MessagingActionDto.DistanceUpdate -> MessagingAction.DistanceUpdate(distanceMeters)
        MessagingActionDto.Finish -> MessagingAction.Finish
        is MessagingActionDto.HeartRateUpdate -> MessagingAction.HeartRateUpdate(heartRate)
        MessagingActionDto.Pause -> MessagingAction.Pause
        MessagingActionDto.StartOrResume -> MessagingAction.StartOrResume
        is MessagingActionDto.TimeUpdate -> MessagingAction.TimeUpdate(elapsedDuration)
        MessagingActionDto.Trackable -> MessagingAction.Trackable
        MessagingActionDto.Untrackable -> MessagingAction.Untrackable
        MessagingActionDto.CancelRun -> MessagingAction.CancelRun
    }
}