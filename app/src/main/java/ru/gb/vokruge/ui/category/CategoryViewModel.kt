package ru.gb.vokruge.ui.category

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ru.gb.vokruge.di.App
import ru.gb.vokruge.model.MainModel
import ru.gb.vokruge.model.models.CompanyCategory
import javax.inject.Inject

class CategoryViewModel : ViewModel() {

    @Inject
    lateinit var model: MainModel

    var clickPosition = 0
    var needShowSubcategories = MutableLiveData<Boolean>().apply { value = false }
    var categories = MutableLiveData<List<CompanyCategory>>().apply { value = emptyList() }

    init {
        App.appComponent.inject(this)
    }

    fun onCategoryItemClick() {
        needShowSubcategories.value = true
    }

    fun showElements() {
        categories.value = model.loadCategories()
    }
}
