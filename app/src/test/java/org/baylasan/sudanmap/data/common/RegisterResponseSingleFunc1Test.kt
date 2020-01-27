package org.baylasan.sudanmap.data.common

import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.whenever
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.ResponseBody
import org.baylasan.sudanmap.data.user.model.Errors
import org.baylasan.sudanmap.data.user.model.RegisterErrorResponse
import org.baylasan.sudanmap.data.user.model.RegisterResponse
import org.junit.Before
import org.junit.Test
import retrofit2.Converter
import retrofit2.Response

class RegisterResponseSingleFunc1Test {

    private lateinit var converter: Converter<ResponseBody, RegisterErrorResponse>
    private val error = "{\n" +
            "    \"errors\": {\n" +
            "        \"username\": [\n" +
            "            \"The username has already been taken.\"\n" +
            "        ],\n" +
            "        \"email\": [\n" +
            "            \"The email has already been taken.\"\n" +
            "        ],\n" +
            "        \"password\": [\n" +
            "            \"The password field is required.\"\n" +
            "        ]\n" +
            "    }\n" +
            "}"

    @Before
    fun before() {
        converter = mock(verboseLogging = true)

    }

    @Test
    fun test() {
        whenever(converter.convert(any())).thenReturn(
            RegisterErrorResponse(
                Errors(
                    listOf(), listOf(),
                    listOf()
                ), 422
            )
        )
        val func1 = RegisterResponseSingleFunc1<RegisterResponse>(converter)
        func1.apply(
            Response.error<RegisterResponse>(
                401,
                ResponseBody.create("application/json".toMediaTypeOrNull(), error)
            )
        )
            .test()
            .assertError(UnAuthorizedException::class.java)
    }
}