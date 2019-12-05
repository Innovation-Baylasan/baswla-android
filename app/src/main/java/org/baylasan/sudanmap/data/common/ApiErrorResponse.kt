package m7mdra.com.mawgif.data.common

class ApiErrorResponse(val message: String, @field:Transient var httpCode: Int)