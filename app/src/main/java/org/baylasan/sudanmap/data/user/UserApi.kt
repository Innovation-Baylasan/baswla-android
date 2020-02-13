package org.baylasan.sudanmap.data.user

import io.reactivex.Single
import okhttp3.ResponseBody
import org.baylasan.sudanmap.data.SudanMapApi
import org.baylasan.sudanmap.data.common.RegisterRequestMapper
import org.baylasan.sudanmap.data.common.RegisterResponseSingleFunc1
import org.baylasan.sudanmap.data.common.ThrowableSingleFunc1
import org.baylasan.sudanmap.data.user.model.*
import org.baylasan.sudanmap.domain.user.UserRepository
import retrofit2.Converter

class UserApi(
    private val userApi: SudanMapApi.User,
    private val errorConverter: Converter<ResponseBody, RegisterErrorResponse>,
    private val registerRequestMapper: RegisterRequestMapper

) : UserRepository {
    override fun register(registerRequest: RegisterRequest): Single<AuthenticationResponse> {
        return userApi.register(registerRequest)
            .onErrorResumeNext(ThrowableSingleFunc1())
            .flatMap(RegisterResponseSingleFunc1(errorConverter))
    }

    override fun registerCompany(registerCompanyRequest: RegisterCompanyRequest): Single<AuthenticationResponse> {
        return userApi.registerCompany(registerRequestMapper.mapToResponseBody(registerCompanyRequest))
            .onErrorResumeNext(ThrowableSingleFunc1())
            .flatMap(RegisterResponseSingleFunc1(errorConverter))
    }


    override fun login(loginRequest: LoginRequest): Single<AuthenticationResponse> {
        return userApi.login(loginRequest)
            .onErrorResumeNext(ThrowableSingleFunc1())
            .flatMap(RegisterResponseSingleFunc1(errorConverter))
    }
}