package m7mdra.com.mawgif.domain.common

import io.reactivex.Observable
import io.reactivex.Single

interface EmptyRequestUseCase<T> {
    fun execute(): Single<T>
}
