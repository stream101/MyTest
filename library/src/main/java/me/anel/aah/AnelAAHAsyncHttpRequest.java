package me.anel.aah;

import com.loopj.android.http.ResponseHandlerInterface;

import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.AbstractHttpClient;
import org.apache.http.protocol.HttpContext;

/**
 * Created by xinxin on 12/21/16.
 */

public class AnelAAHAsyncHttpRequest extends com.loopj.android.http.AsyncHttpRequest {

    public AnelAAHAsyncHttpRequest(AbstractHttpClient client, HttpContext context,
                                   HttpUriRequest request, ResponseHandlerInterface responseHandler) {
        super(client, context, request, responseHandler);
    }
}
