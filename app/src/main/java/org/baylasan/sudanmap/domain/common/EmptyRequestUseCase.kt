package org.baylasan.sudanmap.domain.common

import io.reactivex.Single

interface EmptyRequestUseCase<T> {
    fun execute(): Single<T>
}
