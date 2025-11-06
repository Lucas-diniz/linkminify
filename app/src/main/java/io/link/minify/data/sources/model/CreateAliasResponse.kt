package io.link.minify.data.sources.model

import com.google.gson.annotations.SerializedName

data class CreateAliasResponse(
    val alias: String?,
    @SerializedName("_links")
    val links: AliasLinks?,
)
