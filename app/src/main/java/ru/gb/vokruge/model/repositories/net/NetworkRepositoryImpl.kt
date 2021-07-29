package ru.gb.vokruge.model.repositories.net

import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import ru.gb.vokruge.model.NetworkRepository
import ru.gb.vokruge.model.models.CompanyCategory
import ru.gb.vokruge.model.models.CompanyInfo
import ru.gb.vokruge.model.repositories.net.models.*

class NetworkRepositoryImpl(
    private val networkStatus: INetworkStatus,
    private val apiData: ApiData
) : NetworkRepository {

    override fun loadCompanies(query: String) = apiData.getData(query)
        .subscribeOn(Schedulers.io())
        .map { mapCompaniesNetByQueryToCompanyInfos(it) }

    override fun loadCompany(id: Int) = apiData.getCompany(id)
        .subscribeOn(Schedulers.io())
        .map { mapCompanyInfoNetToCompanyInfo(it) }

    override fun loadAllCategories(): Single<List<CompanyCategory>> =
        apiData.loadAllCategories()
            .subscribeOn(Schedulers.io())
            .map {
                it.categories.map { categoryNet ->
                    CompanyCategory(
                        categoryNet.id,
                        categoryNet.nameCategory,
                        categoryNet.parent
                    )
                }
            }

    override fun loadCompanies(idSubcategory: Int): Single<List<CompanyInfo>> =
        apiData.loadCompanies(idSubcategory)
            .subscribeOn(Schedulers.io())
            .map { mapCompaniesNetByCategoryToCompanyInfos(it) }

    private fun mapCompaniesNetByQueryToCompanyInfos(response: CompaniesByQueryResponse): List<CompanyInfo> {
        return mapCompanyInfosSmallNetToCompanyInfos(response.companies)
    }

    private fun mapCompaniesNetByCategoryToCompanyInfos(response: CompaniesByCategoryResponse): List<CompanyInfo> {
        return mapCompanyInfosSmallNetToCompanyInfos(response.companies)
    }

    private fun mapCompanyInfoNetToCompanyInfo(companyInfoNet: CompanyInfoNet): CompanyInfo {
        return CompanyInfo(
            companyInfoNet.id,
            companyInfoNet.nameCompany,
            companyInfoNet.description,
            companyInfoNet.address,
            companyInfoNet.categories,
            "",
            null,
            null,
            companyInfoNet.siteLink,
            companyInfoNet.email,
            getPhoneNumbers(companyInfoNet.phoneNumbers)
        )
    }

    private fun getPhoneNumbers(phoneNumbers: List<CompanyPhoneNumbersNet>): List<String> {
        val numbers: ArrayList<String> = ArrayList()
        for (i in phoneNumbers.indices){
            if (phoneNumbers[i].phoneNumber.isNotEmpty()){
                numbers.add(phoneNumbers[i].phoneNumber)
            }
        }
        return numbers
    }

    private fun mapCompanyInfosSmallNetToCompanyInfos(companiesNet: List<CompanyInfoSmallNet>): ArrayList<CompanyInfo> {
        val companies = ArrayList<CompanyInfo>()
        for (companyNet in companiesNet) {
            val company = CompanyInfo(
                companyNet.id,
                companyNet.nameCompany,
                companyNet.description,
                getAddress(companyNet) ?: companyNet.address,
                companyNet.categories,
                "",
                getLatitude(companyNet),
                getLongitude(companyNet),
                null,
                null,
                null
            )
            companies.add(company)
        }
        return companies
    }

    private fun getLatitude(companyNet: CompanyInfoSmallNet): Double? {
        companyNet.coordinates?.let {
            if (it.isNotEmpty()) {
                return it[0].latitude
            }
        }
        return null
    }

    private fun getLongitude(companyNet: CompanyInfoSmallNet): Double? {
        companyNet.coordinates?.let {
            if (it.isNotEmpty()) {
                return it[0].longitude
            }
        }
        return null
    }

    private fun getAddress(companyNet: CompanyInfoSmallNet): String? {
        companyNet.coordinates?.let {
            if (it.isNotEmpty()) {
                return it[0].normalAddress
            }
        }
        return null
    }
}