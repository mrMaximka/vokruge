package ru.gb.vokruge.model.repositories.net.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class CompanyInfoSmallNet(

    @SerializedName("id")
    @Expose
    var id: Int,

    @SerializedName("caption")
    @Expose
    var nameCompany: String,

    @SerializedName("description")
    @Expose
    var description: String,

    @SerializedName("address")
    @Expose
    var address: String,

    @SerializedName("category")
    @Expose
    var categories: List<Int>,

    @SerializedName("coordinates")
    @Expose
    var coordinates: List<CompanyCoordinatesNet>?

)