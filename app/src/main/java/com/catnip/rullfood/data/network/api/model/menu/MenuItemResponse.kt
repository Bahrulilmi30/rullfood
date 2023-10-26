package com.catnip.rullfood.data.network.api.model.menu


import com.google.gson.annotations.SerializedName
import androidx.annotation.Keep
import com.catnip.rullfood.model.Menu

@Keep
data class MenuItemResponse(
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
fun MenuItemResponse.toMenu()= Menu(
    descOfMenu = this.descOfMenu.orEmpty(),
    locationName = this.locationName.orEmpty(),
    locationUrl = this.locationUrl.orEmpty(),
    name = this.name.orEmpty(),
    price = this.price?: 0.0,
    productImgUrl = this.productImgUrl.orEmpty()
)
fun Collection<MenuItemResponse>.toMenuList() = this.map { it.toMenu() }