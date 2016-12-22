package me.anel.okhttp;

import com.squareup.okhttp.Call;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Protocol;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import com.squareup.okhttp.ResponseBody;

import org.apache.http.Header;

import me.anel.AnelSyncClient;
import me.anel.AsyncHttpResponseHandler;


/**
 * Created by xinxin on 7/31/16.
 *
 * This is a Anel implementation of OkHttpClient.
 * It keeps okhttp interfaces (as much as possible), but use AnelClient to execute the requests
 *
 *  Code input:
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
 *  Response response = client.newCallV2(request).execute();
 */


public class AnelOkHttpSyncClient extends OkHttpClient {

    //create a AnelSyncClient which is the real client executing the requests
    protected AnelSyncClient client = new AnelSyncClient();


    //new AnelOkHttpSyncClient instance using string specs.
    public AnelOkHttpSyncClient(String[] specs) {
        //The compiler already did spec conflict checking before
        for (String spec: specs) {
            if (spec.equals("UserReq")) {
                client.setMaxRetriesAndTimeout(2, 0);
            }
            else if (spec.equals("Idempotent")) {
                client.setMaxRetriesAndTimeout(2, 0);
            }
            else if (spec.equals("RealtimeData")) {
                client.setReconnectOnSwitch(true);
            }
            else if (spec.equals("SucceedEventually")) {
                client.setAutoResume(true);
            }
        }
    }

    //change from OkhttpClient(). Take no parameter, reserve the OKhttp's Builder() interface
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

    //xinxin.debug: try to extend Call class

    private AnelOkHttpSyncClient(AnelOkHttpSyncClient fromClient) {
        //Copy a AnelOkHttpSyncClient object. Please note that the real implementer is AnelSyncClient,
        //not AnelOkHttpSyncClient. So we actually copy fromClient.client's attributes
        client.setTimeout(fromClient.client.getResponseTimeout());
    }

    final AnelOkHttpSyncClient copyWithDefaults() {
        AnelOkHttpSyncClient result = new AnelOkHttpSyncClient(this);
        return result;
    }

    @Override
    public Call newCall(Request request) {
        return new AnelOkCall(this, request);
    }


}
