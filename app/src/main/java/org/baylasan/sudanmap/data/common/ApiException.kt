package org.baylasan.sudanmap.data.common

import org.baylasan.sudanmap.data.entity.model.AddEntityResponseError
import org.baylasan.sudanmap.data.event.model.AddEventResponseError
import org.baylasan.sudanmap.data.user.model.RegisterErrorResponse

class ApiException(val apiErrorResponse: ApiErrorResponse) : Exception("ApiException")
class RegistrationResponseException(val registrationResponse: RegisterErrorResponse) : Exception()
class AddEntityResponseException(val addEntityResponseError: AddEntityResponseError) : Exception()
class AddEventResponseException(val addEventResponseError: AddEventResponseError) : Exception()

open class ConnectionException(message: String) : Exception(message)

class ClientConnectionException(message: String) : ConnectionException(message)
class TimeoutConnectionException(message: String) : ConnectionException(message)

class ExceedLimitException : Exception("Exceed limit Exception please try later")

class ResponseConvertException(message: String) : Exception(message)

class UnAuthorizedException : Exception("Unauthorized Exception please login again")

class UnexpectedException(message: String) : Exception(message)
class ResponseException(message: String) : Exception(message)

