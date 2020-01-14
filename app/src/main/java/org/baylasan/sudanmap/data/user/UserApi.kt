package org.baylasan.sudanmap.data.user

import io.reactivex.Single
import okhttp3.ResponseBody
import org.baylasan.sudanmap.data.SudanMapApi
import org.baylasan.sudanmap.data.common.ApiErrorResponse
import org.baylasan.sudanmap.data.common.ResponseSingleFunc1
import org.baylasan.sudanmap.data.common.ThrowableSingleFunc1
import org.baylasan.sudanmap.data.user.model.LoginRequest
import org.baylasan.sudanmap.data.user.model.LoginResponse
import org.baylasan.sudanmap.data.user.model.RegisterRequest
import org.baylasan.sudanmap.data.user.model.RegisterResponse
import org.baylasan.sudanmap.domain.user.UserRepository
import retrofit2.Converter

class UserApi(
    private val userApi: SudanMapApi.User,
    private val errorConverter: Converter<ResponseBody, ApiErrorResponse>
) : UserRepository {
    override fun register(registerRequest: RegisterRequest): Single<RegisterResponse> {
        return userApi.register(registerRequest).onErrorResumeNext(ThrowableSingleFunc1())
            .flatMap(ResponseSingleFunc1(errorConverter))
            .map { it }
    }


    override fun login(loginRequest: LoginRequest): Single<LoginResponse> {
        return userApi.login(loginRequest).onErrorResumeNext(ThrowableSingleFunc1())
            .flatMap(ResponseSingleFunc1(errorConverter))
            .map { it }
    }
}