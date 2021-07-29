package ru.gb.vokruge.ui.detailshort

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ru.gb.vokruge.di.App
import ru.gb.vokruge.model.MainModel
import javax.inject.Inject

class ShortDetailViewModel : ViewModel() {

    val needShowFullDetail = MutableLiveData<Boolean>().apply { value = false }

    @Inject
    lateinit var model: MainModel

    init {
        App.appComponent.inject(this)
    }

    fun onClick() {
        needShowFullDetail.value = true
    }

    fun currentCompany() = model.currentCompany

}
