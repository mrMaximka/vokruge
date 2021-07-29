package ru.gb.vokruge.ui.utils

import android.content.Context
import android.util.DisplayMetrics


object Dimensions {
    fun convertDpToPixel(dp: Float, context: Context): Float {
        return dp * (context.getResources().getDisplayMetrics().densityDpi / DisplayMetrics.DENSITY_DEFAULT)
    }
}