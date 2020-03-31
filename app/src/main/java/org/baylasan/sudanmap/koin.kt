package org.baylasan.sudanmap

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson
import com.google.gson.GsonBuilder
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
import org.baylasan.sudanmap.data.common.*
import org.baylasan.sudanmap.data.entity.EntityApi
import org.baylasan.sudanmap.data.entity.model.AddEntityResponseError
import org.baylasan.sudanmap.data.event.EventApi
import org.baylasan.sudanmap.data.event.model.AddEventRequest
import org.baylasan.sudanmap.data.event.model.AddEventResponseError
import org.baylasan.sudanmap.data.tags.TagsApi
import org.baylasan.sudanmap.data.user.SessionManagerImpl
import org.baylasan.sudanmap.data.user.UserApi
import org.baylasan.sudanmap.data.user.model.RegisterErrorResponse
import org.baylasan.sudanmap.domain.LocationViewModel
import org.baylasan.sudanmap.domain.category.CategoryRepository
import org.baylasan.sudanmap.domain.category.FetchCategoriesUseCase
import org.baylasan.sudanmap.domain.entity.*
import org.baylasan.sudanmap.domain.event.*
import org.baylasan.sudanmap.domain.tags.GetTagsByNameUseCase
import org.baylasan.sudanmap.domain.tags.GetTagsUseCase
import org.baylasan.sudanmap.domain.tags.TagsRepository
import org.baylasan.sudanmap.domain.user.*
import org.baylasan.sudanmap.ui.addentity.AddEntityViewModel
import org.baylasan.sudanmap.ui.addevent.AddEventViewModel
import org.baylasan.sudanmap.ui.auth.login.LoginViewModel
import org.baylasan.sudanmap.ui.auth.signup.RegisterViewModel
import org.baylasan.sudanmap.ui.entitydetails.EntityDetailsViewModel
import org.baylasan.sudanmap.ui.entitysearch.EntitySearchViewModel
import org.baylasan.sudanmap.ui.eventsearch.EventSearchViewModel
import org.baylasan.sudanmap.ui.intro.IntroViewModel
import org.baylasan.sudanmap.ui.layers.MapLayersViewModel
import org.baylasan.sudanmap.ui.main.UserProfileViewModel
import org.baylasan.sudanmap.ui.main.entity.EntityViewModel
import org.baylasan.sudanmap.ui.main.event.EventViewModel
import org.baylasan.sudanmap.ui.myentities.MyEntitiesViewModel
import org.baylasan.sudanmap.ui.myevents.MyEventsViewModel
import org.baylasan.sudanmap.ui.splash.SessionViewModel
import org.koin.android.ext.koin.androidApplication
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.qualifier.named
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit


val appModule = module {
    single<SessionManager> { SessionManagerImpl(get(), get()) }
    single {
        provideGson()
    }
    single<SharedPreferences> {
        androidContext().getSharedPreferences("baswala", Context.MODE_PRIVATE)
    }
    single { provideOkHttpClient(get()) }
    single { provideRetrofit(androidApplication(), get()) }
    single { providePicasso(androidApplication()) }
    single { provideErrorConverter(get()) }
    factory(
        override = true,
        qualifier = named("addEntityErrorConverter")
    ) { provideAddEntityErrorConverter(get()) }
    factory(
        override = true,
        qualifier = named("addEventErrorConverter")
    ) { provideAddEventErrorConverter(get()) }
    viewModel { LocationViewModel(androidApplication()) }
    factory<RequestMapper<AddEventRequest>> { AddEventRequestMapper() }
    factory { AddEntityRequestMapper() }
    factory(qualifier = named("io")) { provideIoScheduler() }
    factory(qualifier = named("main")) { provideMainScheduler() }


}
val introModule = module {
    viewModel { IntroViewModel(get()) }
}
val entitiesModule = module(override = true) {
    factory { get<Retrofit>().create(SudanMapApi.Categories::class.java) }
    factory<EntityRepository> {
        EntityApi(
            get(),
            get(),
            get(named("addEntityErrorConverter")),
            get()
        )
    }
    factory { GetMyEntitiesUseCase(get()) }
    viewModel {
        MyEntitiesViewModel(get(), get())
    }
}
val addEntityModule = module(override = true) {
    factory { get<Retrofit>().create(SudanMapApi.Entities::class.java) }
    factory { get<Retrofit>().create(SudanMapApi.Tags::class.java) }
    factory<EntityRepository> {
        EntityApi(
            get(),
            get(),
            get(named("addEntityErrorConverter")),
            get()
        )
    }
    factory<TagsRepository> { TagsApi(get(), get()) }
    factory { AddEntityUseCase(get()) }
    factory { GetTagsUseCase(get()) }
    factory { GetTagsByNameUseCase(get()) }
    viewModel { AddEntityViewModel(get(), get(), get(), get()) }
}
val eventsModule = module(override = true) {
    factory { GetMyEventUseCase(get()) }
    factory { DeleteEventUseCase(get()) }
    viewModel { MyEventsViewModel(get(), get(), get(), get(named("io")), get(named("main"))) }
}

val eventSearchModule = module(override = true) {
    factory { get<Retrofit>().create(SudanMapApi.Events::class.java) }
    factory<EventRepository> { EventApi(get(), get(named("addEventErrorConverter")), get(), get()) }
    factory { FindEventUseCase(get()) }
    viewModel { EventSearchViewModel(get()) }


}
val addEventModule = module(override = true) {
    factory { get<Retrofit>().create(SudanMapApi.Events::class.java) }
    factory<EventRepository> { EventApi(get(), get(named("addEventErrorConverter")), get(), get()) }
    factory { AddEventUseCase(get()) }
    viewModel { AddEventViewModel(get(), get()) }
}

val entityDetailsModule = module(override = true) {
    factory { get<Retrofit>().create(SudanMapApi.Categories::class.java) }
    factory<EntityRepository> {
        EntityApi(get(), get(), get(named("addEntityErrorConverter")), get())
    }
    factory { GetEntityDetailsUseCase(get()) }
    factory { UnFollowEntityUseCase(get()) }
    factory { FollowEntityUseCase(get()) }
    factory { GetRelatedEntityUseCase(get()) }
    factory { RateEntityUseCase(get()) }
    factory { AddReviewUseCase(get()) }
    factory { GetEntityEventsUseCase(get()) }
    viewModel { EntityDetailsViewModel(get(), get(), get(), get(), get(), get(), get(), get()) }

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
        .loggingEnabled(BuildConfig.DEBUG)
        .build()
}

private fun provideIoScheduler(): Scheduler = Schedulers.io()
private fun provideMainScheduler(): Scheduler = AndroidSchedulers.mainThread()
val homePageModule = module(override = true) {
    viewModel { UserProfileViewModel(get()) }

}
val entityListModule = module(override = true) {
    factory { get<Retrofit>().create(SudanMapApi.Entities::class.java) }
    factory<EntityRepository> {
        EntityApi(
            get(),
            get(),
            get(named("addEntityErrorConverter")),
            get()
        )
    }
    factory { GetEntitiesUseCase(get()) }
    factory { GetNearbyEntitiesUseCase(get()) }
    viewModel { EntityViewModel(get(), get()) }
}
val eventModule = module(override = true) {
    factory { get<Retrofit>().create(SudanMapApi.Events::class.java) }
    factory<EventRepository> { EventApi(get(), get(named("addEventErrorConverter")), get(), get()) }
    factory { GetEventUseCase(get()) }
    viewModel { EventViewModel(get()) }
}
val entitySearchModule = module(override = true) {
    factory { get<Retrofit>().create(SudanMapApi.Entities::class.java) }
    factory<EntityRepository> {
        EntityApi(
            get(),
            get(),
            get(named("addEntityErrorConverter")),
            get()
        )
    }
    factory { FindEntitiesByKeywordUseCase(get()) }
    viewModel { EntitySearchViewModel(get()) }
    factory { GetNearbyEntitiesUseCase(get()) }
    viewModel { EntityViewModel(get(), get()) }
}

val sessionModule = module(override = true) {

    viewModel { SessionViewModel(get()) }
}

val userModule = module(override = true) {
    factory { get<Retrofit>().create(SudanMapApi.User::class.java) }
    factory<UserRepository> { UserApi(get(), get()) }
    factory { UserRegisterUseCase(get()) }
    factory { provideRegisterErrorConverter(get()) }

    viewModel { RegisterViewModel(get(), get(), get(named("io")), get(named("main"))) }
    factory { UserLoginUseCase(get()) }
    viewModel { LoginViewModel(get(), get(), get(named("io")), get(named("main"))) }

}


private fun provideErrorConverter(retrofit: Retrofit) =
    retrofit.responseBodyConverter<ApiErrorResponse>(ApiErrorResponse::class.java, arrayOf())

private fun provideAddEntityErrorConverter(retrofit: Retrofit) =
    retrofit.responseBodyConverter<AddEntityResponseError>(
        AddEntityResponseError::class.java,
        arrayOf()
    )

private fun provideAddEventErrorConverter(retrofit: Retrofit) =
    retrofit.responseBodyConverter<AddEventResponseError>(
        AddEventResponseError::class.java,
        arrayOf()
    )

private fun provideRegisterErrorConverter(retrofit: Retrofit) =
    retrofit.responseBodyConverter<ApiErrorResponse>(RegisterErrorResponse::class.java, arrayOf())

private fun provideRetrofit(androidApplication: Application, okHttpClient: OkHttpClient): Retrofit =
    Retrofit.Builder()
        .baseUrl(androidApplication.resources.getString(R.string.api_url))
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

private fun provideGson(): Gson {
    return GsonBuilder()
        .setLenient()
        .create()
}

class SessionInterceptor(private val sessionManager: SessionManager) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val newRequest = request.newBuilder()

            .header("Authorization", "Bearer ${sessionManager.getToken()}")
            .build()

        return chain.proceed(newRequest)

    }
}
/*
class CacheInterceptor(private val context: Context) : Interceptor{
    override fun intercept(chain: Interceptor.Chain): Response {
    }
}*/
