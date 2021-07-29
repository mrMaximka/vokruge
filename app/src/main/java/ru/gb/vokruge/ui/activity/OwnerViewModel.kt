package ru.gb.vokruge.ui.activity

import androidx.lifecycle.ViewModel
import ru.gb.vokruge.di.App
import ru.gb.vokruge.model.MainModel
import javax.inject.Inject

class OwnerViewModel : ViewModel() {

    @Inject
    lateinit var model: MainModel

    init {
        App.appComponent.inject(this)
    }

    fun onCreate() {
        model.loadAllCategories()
    }

    fun panelState() = model.panelState

    fun onBackSearchClick() = model.onBackSearchClick()

}