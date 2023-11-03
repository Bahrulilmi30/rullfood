package com.catnip.rullfood.model

data class Category(
    val id: Int,
    val categoryImgUrl: String,
    val slug: String?,
    val name: String
)
