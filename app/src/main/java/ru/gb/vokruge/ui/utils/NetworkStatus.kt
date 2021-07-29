package ru.gb.vokruge.ui.utils

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import androidx.annotation.RequiresApi
import ru.gb.vokruge.di.App
import ru.gb.vokruge.model.repositories.net.INetworkStatus
import ru.gb.vokruge.model.repositories.net.INetworkStatus.Status

class NetworkStatus : INetworkStatus {

    override val status: Status
        @RequiresApi(Build.VERSION_CODES.M)
        get() {
            val cm = App.instance!!.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            cm.getNetworkCapabilities(cm.activeNetwork)?.run {
                when {
                    hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> return Status.WIFI
                    hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> return Status.MOBILE
                    hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> return Status.ETHERNET
                    else -> false
                }
            }
            return Status.OFFLINE
        }

    override val isOnline: Boolean
        get() = !status.equals(Status.OFFLINE)

    override val isWifi: Boolean
        get() = status.equals(Status.WIFI)

    override val isEthernet: Boolean
        get() = status.equals(Status.ETHERNET)

    override val isMobile: Boolean
        get() = status.equals(Status.MOBILE)

    override val isOffline: Boolean
        get() = status.equals(Status.OFFLINE)
}