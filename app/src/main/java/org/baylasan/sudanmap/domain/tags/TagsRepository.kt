package org.baylasan.sudanmap.domain.tags

import io.reactivex.Single
import org.baylasan.sudanmap.data.entity.model.Tag

interface TagsRepository {
    fun getTagsByName(name: String): Single<List<Tag>>
    fun getTags(): Single<List<Tag>>

}
