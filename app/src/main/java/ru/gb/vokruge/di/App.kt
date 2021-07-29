package ru.gb.vokruge.di

import androidx.multidex.MultiDexApplication
import com.mapbox.mapboxsdk.Mapbox

class App: MultiDexApplication() {

    companion object{
        lateinit var appComponent:AppComponent
        var instance: App? = null
            private set
    }


    override fun onCreate() {
        super.onCreate()
        initAppComponent()
        // Mapbox Access token
        Mapbox.getInstance(applicationContext, "pk.eyJ1Ijoic3dlZXRpbGRhIiwiYSI6ImNqeXNneTNkMDAwdzczY250Z3A4djhkOTMifQ.1SQyUOnHPqNpmBKXYa-hiQ")
    }

    private fun initAppComponent() {
        appComponent = DaggerAppComponent
            .builder()
            .appModule(AppModule(applicationContext))
            .build()
    }
}