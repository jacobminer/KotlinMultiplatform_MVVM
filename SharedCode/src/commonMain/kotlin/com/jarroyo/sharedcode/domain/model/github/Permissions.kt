package com.jarroyo.sharedcode.domain.model.github

import kotlinx.serialization.Serializable

@Serializable
data class Permissions(
    val admin: Boolean,
    val pull: Boolean,
    val push: Boolean
)