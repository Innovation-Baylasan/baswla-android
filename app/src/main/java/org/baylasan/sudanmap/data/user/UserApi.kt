package org.baylasan.sudanmap.data.user

import io.reactivex.Single
import m7mdra.com.mawgif.data.MawgifApi
import m7mdra.com.mawgif.data.common.ApiErrorResponse

import m7mdra.com.mawgif.domain.user.CreateAccountUseCase
import m7mdra.com.mawgif.domain.user.LoginUseCase
import m7mdra.com.mawgif.domain.user.model.User
import m7mdra.com.mawgif.domain.user.UserRepository
import okhttp3.ResponseBody
import org.baylasan.sudanmap.data.common.ResponseSingleFunc1
import org.baylasan.sudanmap.data.common.ThrowableSingleFunc1
import retrofit2.Converter

class UserApi @Inject constructor(private val user: MawgifApi.User,
                                  private val errorConverter: Converter<ResponseBody, ApiErrorResponse>) : UserRepository {
    override fun createUser(params: CreateAccountUseCase.Request): Single<MawgifApi.User> =
            user.createUser(params).onErrorResumeNext(ThrowableSingleFunc1())
                    .flatMap(ResponseSingleFunc1(errorConverter))


    override fun login(params: LoginUseCase.Request): Single<MawgifApi.User> =
            user.login(params).onErrorResumeNext(ThrowableSingleFunc1())
                    .flatMap(ResponseSingleFunc1(errorConverter))

}
