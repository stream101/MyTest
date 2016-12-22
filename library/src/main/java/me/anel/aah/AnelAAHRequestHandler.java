package me.anel.aah;

import com.loopj.android.http.RequestHandle;

import java.lang.ref.WeakReference;

/**
 * Created by xinxin on 12/21/16.
 */

public class AnelAAHRequestHandler extends RequestHandle {
    private final WeakReference<AnelAAHAsyncHttpRequest> request;

    public AnelAAHRequestHandler(AnelAAHAsyncHttpRequest request) {
        super(request);
        this.request =new WeakReference<AnelAAHAsyncHttpRequest>(request);
    }

}
