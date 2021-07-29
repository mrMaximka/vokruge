package ru.gb.vokruge.ui.subcategory

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ru.gb.vokruge.di.App
import ru.gb.vokruge.model.MainModel
import ru.gb.vokruge.model.models.CompanyCategory
import ru.gb.vokruge.model.models.QueryCompanies
import javax.inject.Inject

class SubcategoryViewModel : ViewModel() {

    @Inject
    lateinit var model: MainModel

    var needShowCompaniesByCategory = MutableLiveData<Boolean>().apply { value = false }
    val subcategories = MutableLiveData<List<CompanyCategory>>().apply { value = emptyList() }
    var idCategory = 0

    init {
        App.appComponent.inject(this)
    }

    fun onSubcategoryClick(idSubcategory: Int) {
        model.queryCompanies.value =
            QueryCompanies(type = QueryCompanies.QueryType.SUBCATEGORY, idCategory = idSubcategory)
        model.panelState.value = MainModel.States.LIST_VISIBLE
        needShowCompaniesByCategory.value = true
    }

    fun loadSubcategories(idCategory: Int) {
        if (idCategory != this.idCategory) {
            this.idCategory = idCategory
            subcategories.value = model.loadSubcategories(idCategory)
        }
    }
}
