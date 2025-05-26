package com.paintology.lite.trace.drawing.Model

import com.paintology.lite.trace.drawing.util.NotificationType

data class NotificationModel(
    val title: String?,
    val text: String?,
    val type: NotificationType,
    val postId: String?,
    val userId: String?
)
