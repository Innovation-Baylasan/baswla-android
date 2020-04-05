package org.baylasan.sudanmap.domain.faq

import io.reactivex.Single
import org.baylasan.sudanmap.domain.common.EmptyRequestUseCase
import org.baylasan.sudanmap.domain.faq.model.Faq

class GetFaqUseCase(private val repository: FaqRepository) : EmptyRequestUseCase<Faq> {
    override fun execute(): Single<Faq> {
        return repository.faq()
    }
}