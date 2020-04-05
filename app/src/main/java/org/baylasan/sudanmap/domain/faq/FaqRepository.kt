package org.baylasan.sudanmap.domain.faq

import io.reactivex.Single
import org.baylasan.sudanmap.domain.faq.model.Faq

interface FaqRepository {
    fun faq(): Single<Faq>
}