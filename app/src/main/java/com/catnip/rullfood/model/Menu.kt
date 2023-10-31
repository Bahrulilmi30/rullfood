package com.catnip.rullfood.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Menu(
    val descOfMenu: String,
    val locationName: String,
    val locationUrl: String,
    val name: String,
    val price: Double,
    val productImgUrl: String
): Parcelable