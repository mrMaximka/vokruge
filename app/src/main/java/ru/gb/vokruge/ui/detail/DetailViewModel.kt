package ru.gb.vokruge.ui.detail

import android.annotation.SuppressLint
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel;
import io.reactivex.android.schedulers.AndroidSchedulers
import ru.gb.vokruge.di.App
import ru.gb.vokruge.model.MainModel
import ru.gb.vokruge.model.models.CompanyInfo
import javax.inject.Inject

class DetailViewModel : ViewModel() {

    var needShowMap = MutableLiveData<Boolean>().apply { value = false }
    var needSetInfo = MutableLiveData<Boolean>().apply { value = false }
    var needShowInfoFragment = MutableLiveData<Boolean>().apply { value = false }

    private var companyInfo: CompanyInfo? = null

    @Inject
    lateinit var model: MainModel       // Инжектим модель

    init {
        App.appComponent.inject(this)
    }

    @SuppressLint("CheckResult")
    fun loadCompany(companyId: Int) {
        model.loadCompany(companyId)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ company ->
                companyInfo = company
                showInfo()
            }, {
                it.localizedMessage
            })
    }

    private fun showInfo() {
        needSetInfo.value = true
    }

    fun fillInfoFragment() {
        needShowInfoFragment.value = true
    }

    fun getInfo(): LiveData<Boolean> {
        return needShowInfoFragment
    }

    fun getCompanyInfo(): CompanyInfo? {
        return companyInfo
    }

}