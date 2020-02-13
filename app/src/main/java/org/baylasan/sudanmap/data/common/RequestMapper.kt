package org.baylasan.sudanmap.data.common

import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import org.baylasan.sudanmap.data.entity.model.AddEntityRequest
import org.baylasan.sudanmap.data.event.model.AddEventRequest
import org.baylasan.sudanmap.data.user.model.RegisterCompanyRequest
import java.util.*

interface RequestMapper<T> {
    fun mapToResponseBody(t: T): List<MultipartBody.Part>
}

class RegisterRequestMapper : RequestMapper<RegisterCompanyRequest> {
    override fun mapToResponseBody(request: RegisterCompanyRequest): List<MultipartBody.Part> {
        val builder = MultipartBody.Builder()
            .setType(MultipartBody.FORM)
            .addFormDataPart("name", request.name)
            .addFormDataPart("email", request.email)
            .addFormDataPart("password", request.password)
            .addFormDataPart("password_confirmation", request.passwordConfirmation)
            .addFormDataPart("register_as", "company")
            .addFormDataPart("location", request.location)
            .addFormDataPart("category", request.categoryId.toString())
            .addFormDataPart("description", request.description)
        if (request.avatar != null) {
            builder.addFormDataPart(
                "avatar",
                "file",
                request.avatar.asRequestBody("application/octet-stream".toMediaTypeOrNull())
            )

        }
        if (request.cover != null) {
            builder.addFormDataPart(
                "cover", "file",
                request.cover.asRequestBody("application/octet-stream".toMediaTypeOrNull())
            )
        }
        return builder.build().parts
    }
}

class AddEntityRequestMapper : RequestMapper<AddEntityRequest> {
    override fun mapToResponseBody(t: AddEntityRequest): List<MultipartBody.Part> {
        val multipartBody = MultipartBody.Builder()
            .setType(MultipartBody.FORM)

            .addFormDataPart("category_id", t.category.toString())
            .addFormDataPart("name", t.name)
            .addFormDataPart(
                "description",
                t.description
            )
            .addFormDataPart("latitude", t.locationLat)
            .addFormDataPart("longitude", t.locationLng)

        if (t.cover != null)
            multipartBody.addFormDataPart(
                "cover", "cover${Date().time}",
                t.cover.asRequestBody("application/octet-stream".toMediaTypeOrNull())
            )
        if (t.avatar != null)
            multipartBody.addFormDataPart(
                "avatar", "avatar${Date().time}",
                t.avatar.asRequestBody("application/octet-stream".toMediaTypeOrNull())
            )
        return multipartBody.build().parts
    }

}

class AddEventRequestMapper : RequestMapper<AddEventRequest> {
    override fun mapToResponseBody(t: AddEventRequest): List<MultipartBody.Part> {
        val requestBuilder = MultipartBody.Builder().setType(MultipartBody.FORM)

            .addFormDataPart("link", t.registrationLink)
            .addFormDataPart("description", t.description)
            .addFormDataPart("start_date", t.startDateTime.toString())
            .addFormDataPart("end_date", t.endDateTime.toString())
            .addFormDataPart("latitude", t.locationLat.toString())
            .addFormDataPart("longitude", t.locationLng.toString())
            .addFormDataPart("name", t.name)
        if (t.entityId != null) {
            requestBuilder.addFormDataPart("entity_id", t.entityId.toString())
        }
        if (t.cover != null) {
            requestBuilder.addFormDataPart(
                "cover", "file",
                t.cover.asRequestBody("application/octet-stream".toMediaTypeOrNull())
            )
        }
        return requestBuilder.build().parts

    }

}