package ru.gb.vokruge.ui.categoryshort

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class ShortCategoryViewModel : ViewModel() {

    var needShowAllCategory = MutableLiveData<Boolean>().apply { value = false }
    var needShowSubCategory = MutableLiveData<Int>().apply { value = 0 }

    fun onSubCategoryClick(id: Int) {
        needShowSubCategory.value = id
    }

    fun onAllCategoryClick() {
        needShowAllCategory.value = true
    }
}
