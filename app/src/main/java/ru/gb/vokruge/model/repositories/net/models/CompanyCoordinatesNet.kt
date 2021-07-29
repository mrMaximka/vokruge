package ru.gb.vokruge.model.repositories.net.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class CompanyCoordinatesNet(

    @SerializedName("id")
    @Expose
    var id: Int,

    @SerializedName("latitude")
    @Expose
    var latitude: Double,

    @SerializedName("longitude")
    @Expose
    var longitude: Double,

    @SerializedName("normal_address")
    @Expose
    var normalAddress: String

)
