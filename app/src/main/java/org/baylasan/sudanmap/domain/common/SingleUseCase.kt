package m7mdra.com.mawgif.domain.common


import io.reactivex.Single

interface SingleUseCase<in Q : RequestValues, T> {
    fun execute(params: Q): Single<T>
}
