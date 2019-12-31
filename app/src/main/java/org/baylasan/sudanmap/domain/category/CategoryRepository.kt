package org.baylasan.sudanmap.domain.category

import io.reactivex.Single
import org.baylasan.sudanmap.data.entity.model.Category

interface CategoryRepository {

    fun getCategories(): Single<List<Category>>
}