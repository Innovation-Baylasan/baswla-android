package org.baylasan.sudanmap.domain.common


import io.reactivex.Single

interface SingleUseCase<in Q : RequestValues, T> {
    fun execute(params: Q): Single<T>
}
