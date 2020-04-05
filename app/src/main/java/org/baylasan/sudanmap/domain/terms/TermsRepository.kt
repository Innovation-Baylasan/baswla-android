package org.baylasan.sudanmap.domain.terms

import io.reactivex.Single
import org.baylasan.sudanmap.domain.terms.model.Terms

interface TermsRepository {
    fun terms() :Single<Terms>
}