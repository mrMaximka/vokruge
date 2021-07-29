package ru.gb.vokruge.model

import io.reactivex.Single
import ru.gb.vokruge.model.models.CompanyCategory
import ru.gb.vokruge.model.models.CompanyInfo

interface NetworkRepository {

    fun loadCompanies(query: String): Single<List<CompanyInfo>>

    fun loadCompany(id: Int): Single<CompanyInfo>

    fun loadAllCategories(): Single<List<CompanyCategory>>

    fun loadCompanies(idSubcategory: Int): Single<List<CompanyInfo>>

}