package com.sugar.steptofood

import android.app.Application
import com.sugar.steptofood.di.AppComponent
import com.sugar.steptofood.di.DaggerAppComponent
import com.sugar.steptofood.di.module.ContextModule

class App : Application() {
    override fun onCreate() {
        super.onCreate()
        appComponent = DaggerAppComponent.builder()
                .contextModule(ContextModule(this))
                .build()
    }

    companion object {
        lateinit var appComponent: AppComponent

        //internet https://steptofoodserver-nplmwvwjii.now.sh
        //local http://10.0.2.2:2134
        const val BASE_URL = "http://10.0.2.2:2134"
    }
}