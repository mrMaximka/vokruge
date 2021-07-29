package ru.gb.vokruge.model.models

data class CompanyInfo(
    val id: Int,
    val name_company: String,
    val description: String,
    val address: String,
    val categories: List<Int>,
    var categoryString: String,
    val latitude: Double?,
    val longitude: Double?,
    val siteLink: String?,
    val email: String?,
    val phoneNumbers: List<String?>?
)