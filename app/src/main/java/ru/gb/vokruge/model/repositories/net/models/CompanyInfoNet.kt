package ru.gb.vokruge.model.repositories.net.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class CompanyInfoNet(

    @SerializedName("id")
    @Expose
    var id: Int,

    @SerializedName("caption")
    @Expose
    var nameCompany: String,

    @SerializedName("logo")
    @Expose
    var logo: String,

    @SerializedName("description")
    @Expose
    var description: String,

    @SerializedName("address")
    @Expose
    var address: String,

    @SerializedName("boss")
    @Expose
    var boss: String,

    @SerializedName("site_link")
    @Expose
    var siteLink: String,

    @SerializedName("email")
    @Expose
    var email: String,

    @SerializedName("category")
    @Expose
    var categories: List<Int>,

    @SerializedName("phone_numbers")
    @Expose
    var phoneNumbers: List<CompanyPhoneNumbersNet>
)