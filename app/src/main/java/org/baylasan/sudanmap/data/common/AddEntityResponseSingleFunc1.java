package org.baylasan.sudanmap.data.common;

import org.baylasan.sudanmap.data.entity.model.AddEntityResponseError;
import org.baylasan.sudanmap.data.user.model.RegisterErrorResponse;

import java.io.IOException;

import io.reactivex.Single;
import io.reactivex.functions.Function;
import okhttp3.ResponseBody;
import retrofit2.Converter;
import retrofit2.Response;

public class AddEntityResponseSingleFunc1<T> implements Function<Response<T>, Single<T>> {
    private Converter<ResponseBody, AddEntityResponseError> converter;

    public AddEntityResponseSingleFunc1(Converter<ResponseBody, AddEntityResponseError> errorConverter) {
        converter = errorConverter;
    }


    @Override
    public Single<T> apply(Response<T> response) {
        if (response.isSuccessful()) {
            return Single.just(response.body());
        } else {
            ResponseBody errorBody = response.errorBody();
            int code = response.code();

            if (code == 401) {
                return Single.error(new UnAuthorizedException());
            }

            if (code == 429) {
                return Single.error(new ExceedLimitException());
            }

            if (code == 422) {
                AddEntityResponseError errorResponse;

                try {
                    errorResponse = converter.convert(errorBody);
                    errorResponse.setHttpCode(code);
                } catch (IOException e) {
                    return Single.error(new ResponseConvertException(e.getMessage()));
                }

                return Single.error(new AddEntityResponseException(errorResponse));
            }

            return Single.error(new UnknownError());
        }
    }
}
