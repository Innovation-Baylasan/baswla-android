package org.baylasan.sudanmap.data.common

import okhttp3.MultipartBody
import org.baylasan.sudanmap.data.event.model.AddEventRequest
import org.baylasan.sudanmap.data.user.model.RegisterCompanyRequest
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.MediaType.Companion.toMediaTypeOrNull

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

class AddEventRequestMapper : RequestMapper<AddEventRequest> {
    override fun mapToResponseBody(t: AddEventRequest): List<MultipartBody.Part> {
        val requestBuilder = MultipartBody.Builder().setType(MultipartBody.FORM)
            .addFormDataPart("entity_id", t.entityId.toString())
            .addFormDataPart("event_picture", "cover.jpg")
            .addFormDataPart("registration_link", t.registrationLink)
            .addFormDataPart("description", t.description)
            .addFormDataPart("application_start_datetime", t.startDateTime.toString())
            .addFormDataPart("application_end_datetime", t.endDateTime.toString())
            .addFormDataPart("latitude", t.locationLat.toString())
            .addFormDataPart("longitude", t.locationLng.toString())
            .addFormDataPart("event_name", t.name)

        if (t.cover != null) {
            requestBuilder.addFormDataPart(
                "cover", "file",
                t.cover.asRequestBody("application/octet-stream".toMediaTypeOrNull())
            )
        }
        return requestBuilder.build().parts

    }

}