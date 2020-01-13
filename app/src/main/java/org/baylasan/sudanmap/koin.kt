package org.baylasan.sudanmap

import android.app.Application
import android.content.Context
import com.squareup.picasso.OkHttp3Downloader
import com.squareup.picasso.Picasso
import io.reactivex.Scheduler
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.logging.HttpLoggingInterceptor
import org.baylasan.sudanmap.data.SudanMapApi
import org.baylasan.sudanmap.data.category.CategoryApi
import org.baylasan.sudanmap.data.common.ApiErrorResponse
import org.baylasan.sudanmap.data.entity.EntityApi
import org.baylasan.sudanmap.domain.category.CategoryRepository
import org.baylasan.sudanmap.domain.category.FetchCategoriesUseCase
import org.baylasan.sudanmap.domain.entity.EntityRepository
import org.baylasan.sudanmap.domain.entity.FindEntitiesByKeywordUseCase
import org.baylasan.sudanmap.domain.entity.GetEntitiesUseCase
import org.baylasan.sudanmap.domain.entity.GetNearbyEntitiesUseCase
import org.baylasan.sudanmap.ui.layers.MapLayersViewModel
import org.baylasan.sudanmap.ui.main.place.EntityViewModel
import org.baylasan.sudanmap.ui.search.SearchViewModel
import org.koin.android.ext.koin.androidApplication
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit


val appModule = module {
    single {
        provideOkHttpClient()
    }
    single {
        provideRetrofit(androidApplication(), get())
    }
    single {
        providePicasso(androidApplication())
    }
    single {
        provideErrorConverter(get())
    }
}

val categoryModule = module(override = true) {
    factory { get<Retrofit>().create(SudanMapApi.Categories::class.java) }
    factory<CategoryRepository> { CategoryApi(get(), get()) }
    factory { FetchCategoriesUseCase(get()) }
    viewModel { MapLayersViewModel(get()) }

}

private fun providePicasso(context: Context): Picasso {
    return Picasso.Builder(context)
        .downloader(OkHttp3Downloader(context))
        .build()
}

private fun provideIoScheduler(): Scheduler = Schedulers.io()
private fun provideMainSchudler(): Scheduler = AndroidSchedulers.mainThread()

val entityListModule = module(override = true) {
    factory { get<Retrofit>().create(SudanMapApi.Entities::class.java) }
    factory<EntityRepository> { EntityApi(get(), get()) }
    factory { GetEntitiesUseCase(get()) }
    factory { GetNearbyEntitiesUseCase(get()) }
    viewModel {
        EntityViewModel(
            get(),
            get()
        )
    }
}
val searchModule = module(override = true) {
    factory { get<Retrofit>().create(SudanMapApi.Entities::class.java) }
    factory<EntityRepository> { EntityApi(get(), get()) }
    factory { FindEntitiesByKeywordUseCase(get()) }
    viewModel { SearchViewModel(get()) }
    factory { GetNearbyEntitiesUseCase(get()) }
    viewModel {
        EntityViewModel(get(), get())
    }
}

private fun provideErrorConverter(retrofit: Retrofit) =
    retrofit.responseBodyConverter<ApiErrorResponse>(ApiErrorResponse::class.java, arrayOf())

private fun provideRetrofit(androidApplication: Application, okHttpClient: OkHttpClient): Retrofit =
    Retrofit.Builder()
        .baseUrl(androidApplication.resources.getString(R.string.base_url))
        .addConverterFactory(GsonConverterFactory.create())
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
        .client(okHttpClient)
        .build()


private fun provideOkHttpClient(): OkHttpClient = OkHttpClient.Builder()
    .addNetworkInterceptor(HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    })
    .retryOnConnectionFailure(true)
    .addInterceptor(okHttpInterceptor())
    .writeTimeout(0, TimeUnit.SECONDS)
    .build()

private fun okHttpInterceptor() = Interceptor { chain ->
    val request: Request = chain.request().newBuilder()
        .addHeader("Connection", "close")
        .build()
    chain.proceed(request)
}
