package io.link.minify.domain.entity

import java.util.UUID

data class MinifyLink(
    val id: String = UUID.randomUUID().toString(),
    val url: String,
    val alias: String,
    val shortUrl: String,
    val timestamp: Long = System.currentTimeMillis(),
)