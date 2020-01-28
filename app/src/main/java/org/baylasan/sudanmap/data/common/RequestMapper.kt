package org.baylasan.sudanmap.data.common

import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import org.baylasan.sudanmap.data.user.model.RegisterCompanyRequest

interface RequestMapper<T> {
    fun mapToResponseBody(t: T): List<MultipartBody.Part>
}

class RegisterRequestMapper : RequestMapper<RegisterCompanyRequest> {
    override fun mapToResponseBody(request: RegisterCompanyRequest): List<MultipartBody.Part> {
        return MultipartBody.Builder()
            .setType(MultipartBody.FORM)
            .addFormDataPart("name", "Tester")
            .addFormDataPart("username", "tester123")
            .addFormDataPart("email", "tester@gmail.com")
            .addFormDataPart("password", "tester123")
            .addFormDataPart("password_confirmation", "tester123")
            .addFormDataPart("register_as", "1")
            .addFormDataPart("location", "12.32323,32.00992")
            .addFormDataPart("category", "1")
          /*  .addFormDataPart(
                "avatar",
                "file",
                request.avatar.asRequestBody("application/octet-stream".toMediaTypeOrNull())
            )
            .addFormDataPart(
                "cover", "file",
                request.cover.asRequestBody("application/octet-stream".toMediaTypeOrNull())
            )*/
            .addFormDataPart("description", "This is a description")
            .build()
            .parts

    }
}