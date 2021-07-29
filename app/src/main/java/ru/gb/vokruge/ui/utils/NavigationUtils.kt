package ru.gb.vokruge.ui.utils

import android.os.Bundle
import android.view.View
import androidx.lifecycle.MutableLiveData
import androidx.navigation.Navigation

object NavigationUtils {

    fun observerNavigation(view: View?, value: Boolean, liveBoolean: MutableLiveData<Boolean>, actionId: Int) {
        if (value) {
            view?.let { Navigation.findNavController(view).navigate(actionId) }
            liveBoolean.value = false
        }
    }

    fun observerNavigation(
        view: View?,
        value: Boolean,
        liveBoolean: MutableLiveData<Boolean>,
        actionId: Int,
        bundle: Bundle
    ) {
        if (value) {
            view?.let { Navigation.findNavController(view).navigate(actionId, bundle) }
            liveBoolean.value = false
        }
    }
}