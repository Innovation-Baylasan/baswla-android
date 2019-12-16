package org.baylasan.sudanmap.data.common

class ApiErrorResponse(val message: String, @field:Transient var httpCode: Int)