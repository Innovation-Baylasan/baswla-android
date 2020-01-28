package org.baylasan.sudanmap.domain.user

import io.reactivex.Single
import org.baylasan.sudanmap.data.user.model.*
import org.baylasan.sudanmap.domain.common.RequestValues
import org.baylasan.sudanmap.domain.common.SingleUseCase

class CompanyRegisterUseCase(private val userRepository: UserRepository) :SingleUseCase<CompanyRegisterUseCase.Params ,RegisterResponse> {

    class Params (val registerRequest: RegisterCompanyRequest)  : RequestValues

    override fun execute(params: Params): Single<RegisterResponse> {
        return userRepository.registerCompany(params.registerRequest)
    }
}


