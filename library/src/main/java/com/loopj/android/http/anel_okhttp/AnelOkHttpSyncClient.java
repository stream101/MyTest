package com.loopj.android.http.anel_okhttp;

import com.loopj.android.http.AnelSyncClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.apache.http.Header;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Protocol;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

/**
 * Created by xinxin on 7/31/16.
 *
 * This is a Anel implementation of OkHttpClient.
 *
 * Code input:
 *
 *  OkHttpClient.Builder okBuilder = new OkHttpClient.Builder();
 *  OkHttpClient client = okBuilder.build();
 *  Request request = new Request.Builder().url(logUrl).build();
 *  Response response = client.newCall(request).execute();
 *
 *  Code output:
 *
 *  AnelOkHttpClient.Builder okBuilder = new AnelOkHttpClient.Builder(); // changed
 *  OkHttpClient client = new AnelOkHttpClient(okBuilder);  // changed
 *  Request request = new Request.Builder().url(logUrl).build();
 *  Response response = client.newCall(request).execute();
 */

//
public class AnelOkHttpSyncClient extends OkHttpClient {
    private AnelSyncClient client = new AnelSyncClient();


    public AnelOkHttpSyncClient() {
        this(new Builder());
    }


    private AnelOkHttpSyncClient (Builder builder) {
        //Instantiate AnelClient client.
        // We should translate builder into AnelClient attributes here
        client.setConnectTimeout(builder.connectTimeout);
        int valTimeout = Math.max(builder.readTimeout, builder.writeTimeout);
        client.setTimeout(valTimeout);
        client.setResponseTimeout(valTimeout);

        //Accordingly, set
    }


    // Can't inheritate OkHttp's final builder class, so we have to construct our own.
    // We keep all the interfaces of OkHttpClient.Builder.
    public static final class Builder {
        final int DEFAULT_TIMEOUT = 30000;
        final boolean DEFAULT_RETRY_ONFAILURE = true;
        boolean retryOnConnectionFailure;
        int connectTimeout;
        int readTimeout;
        int writeTimeout;

        public Builder() {
            retryOnConnectionFailure = DEFAULT_RETRY_ONFAILURE;
            connectTimeout = DEFAULT_TIMEOUT;
            readTimeout = DEFAULT_TIMEOUT;
            writeTimeout = DEFAULT_TIMEOUT;
        }



        public AnelOkHttpSyncClient build() {
            return new AnelOkHttpSyncClient(this);
        }
    }

    // Call is final static class, so we can't inherit it.
    // The troublesome thing is that newCall just init a Http Engine, and call.execute() really executes the requests.
    // In most other libraries, there is no newCall() step. My first thinking is to combine these two calls,
    // rewrite "Call newCall(request) and Response Call.execute()" to "Response newCallV2(request)". The rewrittend
    // newCallV2 will execute the request and return OKHttp.Response type. The compiler will do this translation.
    public Response newCallV2(Request request) {
        String url = request.url().toString(); //TBD: is it correct?
        //variables to construct before
        final Response[] response = new Response[1];
        final Request httpRequest = request;

        if (request.method().equals("GET")) {
            client.get(url, new AsyncHttpResponseHandler() {

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

    // For simplicity, we only add code and body for response.
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
