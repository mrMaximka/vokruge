package ru.gb.vokruge.model.models

data class QueryCompanies(val type: QueryType, val idCategory: Int = 0, val query: String = "") {
    enum class QueryType { SUBCATEGORY, SEARCH_STRING }
}