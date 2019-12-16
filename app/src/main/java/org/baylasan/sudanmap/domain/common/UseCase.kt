package org.baylasan.sudanmap.domain.common


import io.reactivex.Observable

interface UseCase<in Q : RequestValues, T> {

    fun execute(requestValues: Q): Observable<T>


}
