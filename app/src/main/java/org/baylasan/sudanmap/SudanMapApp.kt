package org.baylasan.sudanmap

import android.app.Application
import android.content.Context
import androidx.multidex.MultiDex
import androidx.multidex.MultiDexApplication

class SudanMapApp :Application() {


    override fun onCreate() {
        super.onCreate()

    }

    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(base)
        MultiDex.install(this)
    }
}