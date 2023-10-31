package com.catnip.rullfood.data.network.api.model.category


import com.google.gson.annotations.SerializedName
import androidx.annotation.Keep
import com.catnip.rullfood.data.network.api.model.menu.toMenu
import com.catnip.rullfood.data.network.api.model.menu.toMenuList
import com.catnip.rullfood.model.Category

@Keep
data class CategoryItemResponse(
    @SerializedName("id")
    val id: Int?,
    @SerializedName("imgUrlCategory")
    val imgUrlCategory: String?,
    @SerializedName("nameOfCategory")
    val nameOfCategory: String?,
    @SerializedName("slug")
    val slug: String?
)

fun CategoryItemResponse.toCategory() = Category(
    id = this.id?: 0,
    categoryImgUrl = this.imgUrlCategory.orEmpty(),
    name = this.nameOfCategory.orEmpty(),
    slug = this.slug.orEmpty()
)

fun Collection<CategoryItemResponse>.toCategoryList() = this.map { it.toCategory() }