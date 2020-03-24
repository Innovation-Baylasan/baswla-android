package org.baylasan.sudanmap.data.common

import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import org.baylasan.sudanmap.data.entity.model.AddEntityRequest
import org.baylasan.sudanmap.data.event.model.AddEventRequest
import java.util.*

interface RequestMapper<T> {
    fun mapToResponseBody(request: T): List<MultipartBody.Part>
}


class AddEntityRequestMapper : RequestMapper<AddEntityRequest> {
    override fun mapToResponseBody(request: AddEntityRequest): List<MultipartBody.Part> {
        val multipartBody = MultipartBody.Builder()
            .setType(MultipartBody.FORM)
            .addFormDataPart("category_id", request.category.toString())
            .addFormDataPart("name", request.name)
            .addFormDataPart(
                "description",
                request.description
            )
            .addFormDataPart("latitude", request.locationLat)
            .addFormDataPart("longitude", request.locationLng)


        if (request.cover != null)
            multipartBody.addFormDataPart(
                "cover", "cover${Date().time}",
                request.cover.asRequestBody("application/octet-stream".toMediaTypeOrNull())
            )
        if (request.avatar != null)
            multipartBody.addFormDataPart(
                "avatar", "avatar${Date().time}",
                request.avatar.asRequestBody("application/octet-stream".toMediaTypeOrNull())
            )

        request.tags.forEachIndexed { index, s ->
            multipartBody.addFormDataPart("tags[$index]", s)
        }
        return multipartBody.build().parts
    }

}

class AddEventRequestMapper : RequestMapper<AddEventRequest> {
    override fun mapToResponseBody(request: AddEventRequest): List<MultipartBody.Part> {
        val requestBuilder = MultipartBody.Builder().setType(MultipartBody.FORM)

            .addFormDataPart("link", request.registrationLink)
            .addFormDataPart("description", request.description)
            .addFormDataPart("start_date", request.startDateTime.toString())
            .addFormDataPart("end_date", request.endDateTime.toString())
            .addFormDataPart("latitude", request.locationLat.toString())
            .addFormDataPart("longitude", request.locationLng.toString())
            .addFormDataPart("name", request.name)
        if (request.entityId != null) {
            requestBuilder.addFormDataPart("entity_id", request.entityId.toString())
        }
        if (request.cover != null) {
            requestBuilder.addFormDataPart(
                "cover", "file",
                request.cover.asRequestBody("application/octet-stream".toMediaTypeOrNull())
            )
        }
        return requestBuilder.build().parts

    }

}