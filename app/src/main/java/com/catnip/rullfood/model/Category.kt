package com.catnip.rullfood.model

import java.util.UUID

data class Category(
    val id: Int,
    val categoryImgUrl: String,
    val slug: String?,
    val name: String
)