package me.anel.okhttp;

import com.squareup.okhttp.Call;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.Protocol;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import com.squareup.okhttp.ResponseBody;

import org.apache.http.Header;

import java.io.IOException;

import me.anel.AsyncHttpResponseHandler;

/**
 * Created by xinxin on 12/22/16.
 */

public class AnelOkCall extends Call {
    private final AnelOkHttpSyncClient anelOkClient;
    Request originalRequest;

    public AnelOkCall(AnelOkHttpSyncClient client, Request originalRequest) {
        super(client, originalRequest);
        this.anelOkClient = client.copyWithDefaults();   //Get a AnelOkHttpSyncClient client copy
        this.originalRequest = originalRequest;
    }

    @Override
    public Response execute() throws IOException {
        //Need to return Response type
        String url = originalRequest.url().toString(); //TBD: is it correct?
        //variables to construct before
        final Response[] response = new Response[1];
        final Request httpRequest = originalRequest;

        if (originalRequest.method().equals("GET")) {
            anelOkClient.client.get(url, new AsyncHttpResponseHandler() {

                @Override
                public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                    // we need to transform Anel response to OKhttp response.
                    response[0] =  transformResponseFromAnelToOkHttp(httpRequest, statusCode, headers, responseBody, null);
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                    response[0] = transformResponseFromAnelToOkHttp(httpRequest, statusCode, headers, responseBody, error);
                }
            });
        }
        //TBD: POST request

        return response[0];
    }

    private Response transformResponseFromAnelToOkHttp (Request httpRequest, int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

        //TBD: Change null !!
        ResponseBody body = ResponseBody.create(MediaType.parse("text/plain;charset=utf-8"), responseBody);

        return new Response.Builder().
                code(statusCode).
                body(body).
                protocol(Protocol.HTTP_1_1).
                request(httpRequest).
                build();
    }
}
