package org.baylasan.sudanmap.domain.common


import io.reactivex.Completable

interface CompletableUseCase<in T : RequestValues> {
    fun execute(params: T): Completable
}