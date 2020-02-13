package org.baylasan.sudanmap.domain.user

import io.reactivex.Single
import org.baylasan.sudanmap.data.user.model.*
import org.baylasan.sudanmap.domain.common.RequestValues
import org.baylasan.sudanmap.domain.common.SingleUseCase

open class CompanyRegisterUseCase(private val userRepository: UserRepository) :SingleUseCase<CompanyRegisterUseCase.Params ,AuthenticationResponse> {

    class Params (val registerRequest: RegisterCompanyRequest)  : RequestValues

    override fun execute(params: Params): Single<AuthenticationResponse> {
        return userRepository.registerCompany(params.registerRequest)
    }
}


