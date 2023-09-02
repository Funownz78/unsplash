package com.example.imageshering

import android.app.Application
import com.example.imageshering.di.AppModule
import com.example.imageshering.di.DaggerAuthModule
import com.example.imageshering.di.DaggerComponent
import com.example.imageshering.di.DaggerDaggerComponent

private const val CLIENT_ID = "w_Ow_7HSlr-d3NDDN2LSAG2pi-hzbMQ5TGaFviEFOIw"
private const val CLIENT_SECRET = "R2UXAzS84cEezaOnGZ3-F96xA0iqmh5xU9BVdoyirR4"

class App : Application() {
    override fun onCreate() {
        super.onCreate()
        daggerComponent = DaggerDaggerComponent
            .builder()
            .daggerAuthModule(DaggerAuthModule(this))
            .appModule(AppModule(this))
            .build()
    }

    companion object {
        lateinit var daggerComponent: DaggerComponent
    }
}