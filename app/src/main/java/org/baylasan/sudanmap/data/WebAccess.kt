package org.baylasan.sudanmap.data

import android.util.Log
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.baylasan.sudanmap.BuildConfig
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory


object WebAccess {
    val apiService: SudanMapApi.Entities by lazy {
        Log.d("WebAccess", "Creating retrofit client")

        val logging = HttpLoggingInterceptor()
        //logging.apply { logging.level = HttpLoggingInterceptor.Level.BODY }


        if (BuildConfig.DEBUG) {
            logging.level = HttpLoggingInterceptor.Level.BODY
        } else {
            logging.level = HttpLoggingInterceptor.Level.NONE
        }


        val httpClient = OkHttpClient.Builder()
        httpClient.addInterceptor(logging)


        val retrofit = Retrofit.Builder()
            // The 10.0.2.2 address routes request from the Android emulator
            // to the localhost / 127.0.0.1 of the host PC
            .baseUrl(BASE_URL)
            .client(httpClient.build())
            // Moshi maps JSON to classes
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())

            // The call adapter handles threads
            //  .addCallAdapterFactory(CoroutineCallAdapterFactory())
            .build()

        // Create Retrofit client
        return@lazy retrofit.create(SudanMapApi.Entities::class.java)
    }
}