package ru.gb.vokruge.model.repositories.net.models

import com.google.gson.annotations.SerializedName

data class CompaniesByQueryResponse(

    @SerializedName("companies")
    var companies: List<CompanyInfoSmallNet>
)
