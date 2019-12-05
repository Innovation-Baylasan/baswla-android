package org.baylasan.sudanmap.data.common;

import android.text.TextUtils;

import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

import io.reactivex.Single;
import io.reactivex.functions.Function;
import m7mdra.com.mawgif.data.common.ClientConnectionException;
import m7mdra.com.mawgif.data.common.TimeoutConnectionException;
import m7mdra.com.mawgif.data.common.UnexpectedException;
import retrofit2.Response;
public class ThrowableSingleFunc1<T> implements Function<Throwable, Single<? extends Response<T>>> {
    private static final String TAG = "ThrowableSingleFunc1";


    @Override
    public Single<? extends Response<T>> apply(Throwable throwable) throws Exception {
        String message = throwable.getMessage();
        if (TextUtils.isEmpty(message)) {
            message = "";
        }
        if (throwable instanceof ConnectException ||
                throwable instanceof UnknownHostException) {
            return Single.error(new ClientConnectionException(message));
        } else if (throwable instanceof SocketTimeoutException) {
            return Single.error(new TimeoutConnectionException(message));
        }

        return Single.error(new UnexpectedException(message));
    }

}
