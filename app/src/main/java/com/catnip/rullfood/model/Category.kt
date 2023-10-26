package com.catnip.rullfood.model

import java.util.UUID

data class Category(
    val id: String = UUID.randomUUID().toString(),
    val categoryImgUrl: String,
    val slug: String?,
    val name: String
)