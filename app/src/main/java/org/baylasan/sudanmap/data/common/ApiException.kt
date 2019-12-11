package org.baylasan.sudanmap.data.common

class ApiException(val apiErrorResponse: ApiErrorResponse) : Exception("ApiException")

open class ConnectionException(message: String) : Exception(message)

class ClientConnectionException(message: String) : ConnectionException(message)
class TimeoutConnectionException(message: String) : ConnectionException(message)

class ExceedLimitException : Exception("Exceed limit Exception please try later")

class ResponseConvertException(message: String) : Exception(message)

class UnAuthorizedException : Exception("Unauthorized Exception please login again")

class UnexpectedException(message: String) : Exception(message)

