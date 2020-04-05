package org.baylasan.sudanmap.domain.policy

import io.reactivex.Single
import org.baylasan.sudanmap.domain.common.EmptyRequestUseCase
import org.baylasan.sudanmap.domain.policy.model.Policy

class GetPolicyUseCase(private val repository: PolicyRepository) : EmptyRequestUseCase<Policy> {
    override fun execute(): Single<Policy> {
        return repository.policy()
    }
}