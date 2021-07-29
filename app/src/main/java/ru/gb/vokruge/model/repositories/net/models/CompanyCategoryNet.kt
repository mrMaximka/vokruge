package ru.gb.vokruge.model.repositories.net.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class CompanyCategoryNet(

    @SerializedName("id")
    @Expose
    var id: Int,

    @SerializedName("caption")
    @Expose
    var nameCategory: String,

    @SerializedName("parent")
    @Expose
    var parent: Int?

)