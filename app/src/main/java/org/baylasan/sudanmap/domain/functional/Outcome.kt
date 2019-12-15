package org.baylasan.sudanmap.domain.functional

import org.baylasan.sudanmap.domain.entity.model.EntityResponseDto

sealed class Outcome<out T : Any> {
    data class Success<out T : Any>(val value: T) : Outcome<T>()
    data class Error(val message: String, val cause: Exception? = null) : Outcome<Nothing>()
}



lateinit var outcome :Outcome<EntityResponseDto>


