package io.link.minify.domain.entity

data class MinifyLink(
    val link: Link,
    val alias: String,
    val shortUrl: String,
    val clickCount: Int = 0
)