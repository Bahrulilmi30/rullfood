package com.catnip.rullfood.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Product(
    val id: Int? = null,
    val name: String,
    val price: Double,
    val weightInKg: Double,
    val supplierName: String,
    val rating: Double,
    val desc: String,
    val productImgUrl: String
) : Parcelable