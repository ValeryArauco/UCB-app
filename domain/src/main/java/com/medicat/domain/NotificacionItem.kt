package com.medicat.domain
import kotlinx.serialization.Serializable

@Serializable
data class NotificacionItem(
    val id: Int,
    val userId: Int,
    val competitionId: Int,
    val type: String,
    val title: String,
    val message: String,
    val isRead: Boolean = false,
    val sentAt: String,
)
