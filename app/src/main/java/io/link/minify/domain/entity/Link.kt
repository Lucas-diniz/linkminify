package io.link.minify.domain.entity

import java.util.UUID

data class Link(
    val id: String = UUID.randomUUID().toString(),
    val timestamp: Long = System.currentTimeMillis(),
    val url: String,
)
