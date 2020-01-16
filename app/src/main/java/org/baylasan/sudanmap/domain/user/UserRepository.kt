package org.baylasan.sudanmap.domain.user

import io.reactivex.Single
import org.baylasan.sudanmap.data.user.model.LoginRequest
import org.baylasan.sudanmap.data.user.model.LoginResponse
import org.baylasan.sudanmap.data.user.model.RegisterRequest
import org.baylasan.sudanmap.data.user.model.RegisterResponse

interface UserRepository {
    fun register(registerRequest: RegisterRequest): Single<RegisterResponse>
    fun login(loginRequest: LoginRequest): Single<LoginResponse>
}