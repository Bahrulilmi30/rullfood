package com.catnip.rullfood.data.network.api.model.menu

import androidx.annotation.Keep
import com.catnip.rullfood.model.Menu
import com.google.gson.annotations.SerializedName

@Keep
data class MenuItemResponse(
    @SerializedName("id")
    val id: Int,
    @SerializedName("descOfMenu")
    val descOfMenu: String?,
    @SerializedName("locationName")
    val locationName: String?,
    @SerializedName("locationUrl")
    val locationUrl: String?,
    @SerializedName("name")
    val name: String?,
    @SerializedName("price")
    val price: Double?,
    @SerializedName("productImgUrl")
    val productImgUrl: String?
)
fun MenuItemResponse.toMenu() = Menu(
    id = this.id,
    descOfMenu = this.descOfMenu.orEmpty(),
    locationName = this.locationName.orEmpty(),
    locationUrl = this.locationUrl.orEmpty(),
    name = this.name.orEmpty(),
    price = this.price ?: 0.0,
    productImgUrl = this.productImgUrl.orEmpty()
)
fun Collection<MenuItemResponse>.toMenuList() = this.map { it.toMenu() }
