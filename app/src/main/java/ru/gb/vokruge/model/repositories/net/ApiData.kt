package ru.gb.vokruge.model.repositories.net


import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Path
import ru.gb.vokruge.model.repositories.net.models.CategoryResponce
import ru.gb.vokruge.model.repositories.net.models.CompaniesByCategoryResponse
import ru.gb.vokruge.model.repositories.net.models.CompaniesByQueryResponse
import ru.gb.vokruge.model.repositories.net.models.CompanyInfoNet

interface ApiData {

    @GET("api/company/{id}")
    fun getCompany(@Path("id") id: Int): Single<CompanyInfoNet>

    @GET("api/categories/all")
    fun loadAllCategories(): Single<CategoryResponce>

    @GET("api/categories/{idSubcategory}/companies")
    fun loadCompanies(
        @Path("idSubcategory") idSubcategory: Int
    ): Single<CompaniesByCategoryResponse>

    @GET("/api/company/search/{query}")
    fun getData(
        @Path("query") query: String
    ): Single<CompaniesByQueryResponse>

}
