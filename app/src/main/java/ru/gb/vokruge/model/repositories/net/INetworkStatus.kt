package ru.gb.vokruge.model.repositories.net

interface INetworkStatus {

    val status: Status

    val isOnline: Boolean

    val isWifi: Boolean

    val isEthernet: Boolean

    val isMobile: Boolean

    val isOffline: Boolean

    enum class Status {
        WIFI,
        MOBILE,
        ETHERNET,
        OTHER,
        OFFLINE
    }
}