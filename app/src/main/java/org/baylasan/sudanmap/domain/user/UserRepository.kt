package m7mdra.com.mawgif.domain.user

import io.reactivex.Single
import m7mdra.com.mawgif.domain.user.model.User

interface UserRepository {
    fun createUser(params: CreateAccountUseCase.Request): Single<User>
    fun login(params: LoginUseCase.Request): Single<User>
}
