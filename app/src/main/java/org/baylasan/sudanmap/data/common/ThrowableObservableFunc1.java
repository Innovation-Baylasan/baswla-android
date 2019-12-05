package org.baylasan.sudanmap.data.common;

import android.text.TextUtils;
import android.util.Log;

import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

import io.reactivex.Observable;
import io.reactivex.functions.Function;
import m7mdra.com.mawgif.data.common.ClientConnectionException;
import m7mdra.com.mawgif.data.common.TimeoutConnectionException;
import m7mdra.com.mawgif.data.common.UnexpectedException;
import retrofit2.Response;
public class ThrowableObservableFunc1<T> implements Function<Throwable, Observable<? extends Response<T>>> {
    private static final String TAG = "ThrowableObservable";


    @Override
    public Observable<? extends Response<T>> apply(Throwable throwable) throws Exception {
        String message = throwable.getMessage();
        if (TextUtils.isEmpty(message)) {
            message = "";
        }

        Log.v(TAG, "ThrowableObservableFunc1");
        Log.v(TAG, "error: " + message);

        if (throwable instanceof ConnectException ||
                throwable instanceof UnknownHostException) {
            return Observable.error(new ClientConnectionException(message));
        } else if (throwable instanceof SocketTimeoutException) {
            return Observable.error(new TimeoutConnectionException(message));
        }

        return Observable.error(new UnexpectedException(message));    }
}
