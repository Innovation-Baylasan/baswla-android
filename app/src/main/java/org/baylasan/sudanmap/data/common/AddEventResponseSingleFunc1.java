package org.baylasan.sudanmap.data.common;

import org.baylasan.sudanmap.data.event.model.AddEventResponseError;

import java.io.IOException;

import io.reactivex.Single;
import io.reactivex.functions.Function;
import okhttp3.ResponseBody;
import retrofit2.Converter;
import retrofit2.Response;

public class AddEventResponseSingleFunc1<T> implements Function<Response<T>, Single<T>> {
    private Converter<ResponseBody, AddEventResponseError> converter;

    public AddEventResponseSingleFunc1(Converter<ResponseBody, AddEventResponseError> errorConverter) {
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
                AddEventResponseError errorResponse;

                try {
                    errorResponse = converter.convert(errorBody);
                    errorResponse.setHttpCode(code);
                } catch (IOException e) {
                    return Single.error(new ResponseConvertException(e.getMessage()));
                }

                return Single.error(new AddEventResponseException(errorResponse));
            }

            return Single.error(new UnknownError());
        }
    }
}
