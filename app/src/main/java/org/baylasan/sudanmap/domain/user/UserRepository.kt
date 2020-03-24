package org.baylasan.sudanmap.domain.user

import io.reactivex.Single
import org.baylasan.sudanmap.data.user.model.*

interface UserRepository {
    fun register(registerRequest: RegisterRequest): Single<AuthenticationResponse>
    fun login(loginRequest: LoginRequest): Single<AuthenticationResponse>
}