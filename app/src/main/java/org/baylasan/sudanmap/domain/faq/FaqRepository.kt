package org.baylasan.sudanmap.domain.faq

import io.reactivex.Single
import org.baylasan.sudanmap.domain.faq.model.Faqs

interface FaqRepository {
    fun faq(): Single<Faqs>
}