package com.jarroyo.sharedcode.domain.model.github

import kotlinx.serialization.Serializable

@Serializable
data class PullRequest(
    val diff_url: String,
    val html_url: String,
    val patch_url: String,
    val url: String
)