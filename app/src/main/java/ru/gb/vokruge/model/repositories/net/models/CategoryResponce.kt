package ru.gb.vokruge.model.repositories.net.models

import com.google.gson.annotations.SerializedName

data class CategoryResponce(
    @SerializedName("categories")
    var categories: List<CompanyCategoryNet>
)