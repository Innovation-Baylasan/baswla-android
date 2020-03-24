package org.baylasan.sudanmap

import android.app.Application
import android.content.Context
import androidx.multidex.MultiDex
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class SudanMapApp : Application() {


    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidLogger()
            androidContext(applicationContext)
            modules(
                listOf(
                    appModule,
                    categoryModule,
                    entityListModule,
                    entitySearchModule,
                    userModule,
                    sessionModule,
                    eventModule,
                    homePageModule,
                    eventsModule,
                    entitiesModule,
                    addEventModule,
                    entityDetailsModule,
                    addEntityModule,
                    introModule,
                    eventSearchModule
                )
            )

        }

    }

    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(base)
        MultiDex.install(this)
    }
}