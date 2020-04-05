package org.baylasan.sudanmap.domain.faq

import io.reactivex.Single
import org.baylasan.sudanmap.domain.common.EmptyRequestUseCase
import org.baylasan.sudanmap.domain.faq.model.Faqs

class GetFaqUseCase(private val repository: FaqRepository) : EmptyRequestUseCase<Faqs> {
    override fun execute(): Single<Faqs> {
        return repository.faq()
    }
}