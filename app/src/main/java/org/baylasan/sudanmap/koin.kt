package org.baylasan.sudanmap

import android.app.Application
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.baylasan.sudanmap.data.SudanMapApi
import org.baylasan.sudanmap.data.category.CategoryApi
import org.baylasan.sudanmap.data.common.ApiErrorResponse
import org.baylasan.sudanmap.domain.category.CategoryRepository
import org.baylasan.sudanmap.domain.category.FetchCategoriesUseCase
import org.baylasan.sudanmap.ui.layers.MapLayersViewModel
import org.koin.android.ext.koin.androidApplication
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

val appModule = module {
    single {
        provideOkHttpClient()
    }
    single {
        provideRetrofit(androidApplication(), get())
    }
    single {
        provideErrorConverter(get())
    }
}

val categoryModule = module {
    factory { get<Retrofit>().create(SudanMapApi.Categories::class.java) }
    factory<CategoryRepository> { CategoryApi(get(), get()) }
    factory {
        FetchCategoriesUseCase(get())
    }
    viewModel {
        MapLayersViewModel(get())
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
    .addNetworkInterceptor(HttpLoggingInterceptor())
    .build()
