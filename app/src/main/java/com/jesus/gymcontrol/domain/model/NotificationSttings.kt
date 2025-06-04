package com.jesus.gymcontrol.domain.model

data class NotificationSettings(
    val newClientEnabled: Boolean,
    val paymentRegisteredEnabled: Boolean,
    val membershipExpirationEnabled: Boolean,
    val weekStartEnabled: Boolean,
    val pushNotificationsEnabled: Boolean
)