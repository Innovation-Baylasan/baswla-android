package org.baylasan.sudanmap.data.common;

import java.io.IOException;

import io.reactivex.Single;
import io.reactivex.functions.Function;
import okhttp3.ResponseBody;
import retrofit2.Converter;
import retrofit2.Response;

public class ResponseSingleFunc1<T> implements Function<Response<T>, Single<T>> {
    private Converter<ResponseBody, ApiErrorResponse> converter;

    public ResponseSingleFunc1(Converter<ResponseBody, ApiErrorResponse> errorConverter) {
        converter = errorConverter;
    }


    @Override
    public Single<T> apply(Response<T> response) {
        if (response.isSuccessful()) {
            return Single.just(response.body());
        } else {
            ResponseBody responseBody = response.errorBody();
            int code = response.code();

            if (code == 401) {
                return Single.error(new UnAuthorizedException());
            }

            if (code == 429) {
                return Single.error(new ExceedLimitException());
            }

            if (code == 422) {
                System.out.println("vvvvvvv" + response.body().toString());
                return Single.error(new ResponseException(response.body().toString()));
            }

            ApiErrorResponse apiErrorResponse;

            try {
                apiErrorResponse = converter.convert(responseBody);
                apiErrorResponse.setHttpCode(code);
            } catch (IOException e) {
                return Single.error(new ResponseConvertException(e.getMessage()));
            }

            return Single.error(new ApiException(apiErrorResponse));
        }
    }
}
