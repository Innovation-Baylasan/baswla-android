package org.baylasan.sudanmap.domain.category

import io.reactivex.Single
import org.baylasan.sudanmap.domain.common.EmptyRequestUseCase
import org.baylasan.sudanmap.data.entity.model.Category

open class FetchCategoriesUseCase(private val categoryRepository: CategoryRepository) :
    EmptyRequestUseCase<List<Category>> {
    override fun execute(): Single<List<Category>> {
        return categoryRepository.getCategories()
    }
}