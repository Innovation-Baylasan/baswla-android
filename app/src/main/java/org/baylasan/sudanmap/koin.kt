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
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor
import org.baylasan.sudanmap.data.SudanMapApi
import org.baylasan.sudanmap.data.category.CategoryApi
import org.baylasan.sudanmap.data.common.ApiErrorResponse
import org.baylasan.sudanmap.data.common.RegisterRequestMapper
import org.baylasan.sudanmap.data.entity.EntityApi
import org.baylasan.sudanmap.data.event.EventApi
import org.baylasan.sudanmap.data.user.SessionManagerImpl
import org.baylasan.sudanmap.data.user.UserApi
import org.baylasan.sudanmap.data.user.model.RegisterErrorResponse
import org.baylasan.sudanmap.domain.LocationViewModel
import org.baylasan.sudanmap.domain.category.CategoryRepository
import org.baylasan.sudanmap.domain.category.FetchCategoriesUseCase
import org.baylasan.sudanmap.domain.entity.*
import org.baylasan.sudanmap.domain.event.EventRepository
import org.baylasan.sudanmap.domain.event.GetEventUseCase
import org.baylasan.sudanmap.domain.user.*
import org.baylasan.sudanmap.ui.auth.login.LoginViewModel
import org.baylasan.sudanmap.ui.auth.signup.RegisterViewModel
import org.baylasan.sudanmap.ui.layers.MapLayersViewModel
import org.baylasan.sudanmap.ui.main.UserProfileViewModel
import org.baylasan.sudanmap.ui.main.event.EventViewModel
import org.baylasan.sudanmap.ui.main.place.EntityViewModel
import org.baylasan.sudanmap.ui.placedetails.PlaceDetailsViewModel
import org.baylasan.sudanmap.ui.placesearch.PlaceSearchViewModel
import org.baylasan.sudanmap.ui.splash.SessionViewModel
import org.koin.android.ext.koin.androidApplication
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit


val appModule = module {
    single<SessionManager> { SessionManagerImpl(get()) }

    single { provideOkHttpClient(get()) }
    single { provideRetrofit(androidApplication(), get()) }
    single { providePicasso(androidApplication()) }
    single { provideErrorConverter(get()) }
    viewModel { LocationViewModel(androidApplication()) }
}

val entityDetailsModule = module(override = true) {
    factory { get<Retrofit>().create(SudanMapApi.Categories::class.java) }
    factory<EntityRepository> { EntityApi(get(), get()) }
    factory { GetEntityDetailsUseCase(get()) }
    factory { UnFollowEntityUseCase(get()) }
    factory { FollowEntityUseCase(get()) }
    factory { AddReviewUseCase(get()) }
    viewModel { PlaceDetailsViewModel(get(), get(), get(), get()) }

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
val homePageModule = module(override = true) {
    viewModel { UserProfileViewModel(get()) }

}
val entityListModule = module(override = true) {
    factory { get<Retrofit>().create(SudanMapApi.Entities::class.java) }
    factory<EntityRepository> { EntityApi(get(), get()) }
    factory { GetEntitiesUseCase(get()) }
    factory { GetNearbyEntitiesUseCase(get()) }
    viewModel { EntityViewModel(get(), get()) }
}
val eventModule = module(override = true) {
    factory { get<Retrofit>().create(SudanMapApi.Events::class.java) }
    factory<EventRepository> { EventApi(get(), get()) }
    factory { GetEventUseCase(get()) }
    viewModel { EventViewModel(get()) }
}
val searchModule = module(override = true) {
    factory { get<Retrofit>().create(SudanMapApi.Entities::class.java) }
    factory<EntityRepository> { EntityApi(get(), get()) }
    factory { FindEntitiesByKeywordUseCase(get()) }
    viewModel { PlaceSearchViewModel(get()) }
    factory { GetNearbyEntitiesUseCase(get()) }
    viewModel { EntityViewModel(get(), get()) }
}
val sessionModule = module(override = true) {

    viewModel { SessionViewModel(get()) }
}

val userModule = module(override = true) {
    factory { RegisterRequestMapper() }
    factory { get<Retrofit>().create(SudanMapApi.User::class.java) }
    factory<UserRepository> { UserApi(get(), get(), get()) }
    factory { UserRegisterUseCase(get()) }
    factory { CompanyRegisterUseCase(get()) }
    factory { provideRegisterErrorConverter(get()) }

    viewModel { RegisterViewModel(get(), get(), get()) }
    factory { UserLoginUseCase(get()) }
    viewModel { LoginViewModel(get(), get()) }

}

private fun provideErrorConverter(retrofit: Retrofit) =
    retrofit.responseBodyConverter<ApiErrorResponse>(ApiErrorResponse::class.java, arrayOf())

private fun provideRegisterErrorConverter(retrofit: Retrofit) =
    retrofit.responseBodyConverter<ApiErrorResponse>(RegisterErrorResponse::class.java, arrayOf())

private fun provideRetrofit(androidApplication: Application, okHttpClient: OkHttpClient): Retrofit =
    Retrofit.Builder()
        .baseUrl(androidApplication.resources.getString(R.string.base_url))
        .addConverterFactory(GsonConverterFactory.create())
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
        .client(okHttpClient)
        .build()


private fun provideOkHttpClient(sessionManager: SessionManager): OkHttpClient =
    OkHttpClient.Builder()
        .addNetworkInterceptor(HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        })
        .retryOnConnectionFailure(true)
        .addNetworkInterceptor(SessionInterceptor(sessionManager))
        .addInterceptor(okHttpInterceptor())
        .writeTimeout(0, TimeUnit.SECONDS)
        .build()

private fun okHttpInterceptor() = Interceptor { chain ->
    val request: Request = chain.request().newBuilder()

        .addHeader("Connection", "close")
        .addHeader("Content-Type", "application/json")
        .addHeader("Accept", "application/json")
        .build()
    chain.proceed(request)
}

class SessionInterceptor(private val sessionManager: SessionManager) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val headers = request.headers.newBuilder()
            .add("Connection", "close")
            .add("Content-Type", "application/json")
            .add("Accept", "application/json")
            .add("Authorization", "Bearer ${sessionManager.getToken()}")
            .build()



        return chain.proceed(request.newBuilder().headers(headers).build())
    }
}