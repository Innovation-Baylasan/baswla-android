package org.baylasan.sudanmap.domain.policy

import io.reactivex.Single
import org.baylasan.sudanmap.domain.policy.model.Policy

interface PolicyRepository {
    fun policy() :Single<Policy>
}