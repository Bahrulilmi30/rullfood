package com.catnip.rullfood.data.network.api.model.category

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName

@Keep
data class CategoryResponse(
    @SerializedName("code")
    val code: Int?,
    @SerializedName("data")
    val data: List<CategoryItemResponse>?,
    @SerializedName("message")
    val message: String?,
    @SerializedName("status")
    val status: Boolean?
)
