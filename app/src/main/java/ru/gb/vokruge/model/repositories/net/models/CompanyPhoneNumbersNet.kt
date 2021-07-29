package ru.gb.vokruge.model.repositories.net.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class CompanyPhoneNumbersNet (

    @SerializedName("caption")
    @Expose
    var caption: String,

    @SerializedName("phone_number")
    @Expose
    var phoneNumber: String,

    @SerializedName("longitude")
    @Expose
    var fax: Boolean
)