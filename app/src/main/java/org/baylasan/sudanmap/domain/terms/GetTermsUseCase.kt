package org.baylasan.sudanmap.domain.terms

import io.reactivex.Single
import org.baylasan.sudanmap.domain.common.EmptyRequestUseCase
import org.baylasan.sudanmap.domain.terms.model.Terms

class GetTermsUseCase (private val repository: TermsRepository): EmptyRequestUseCase<Terms> {
    override fun execute(): Single<Terms> {
        return repository.terms()
    }
}