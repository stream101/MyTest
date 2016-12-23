package me.anel.aah;

import com.loopj.android.http.AsyncHttpClient;

import org.apache.http.Header;

import me.anel.AnelClient;
import me.anel.AsyncHttpResponseHandler;
import me.anel.RequestHandle;

/**
 * Async http client's Anel representation
 */

public class AnelAAHClient extends AsyncHttpClient {
    private AnelClient client = new AnelClient();


    public AnelAAHClient(String[] specs) {
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

    public AnelAAHClient() {

    }

    @Override
    public void setTimeout(int value) {
        client.setTimeout(value);
    }



    @Override
    public com.loopj.android.http.RequestHandle get(String url, com.loopj.android.http.ResponseHandlerInterface responseHandler) {
        final com.loopj.android.http.AsyncHttpResponseHandler originResponseHandler = (com.loopj.android.http.AsyncHttpResponseHandler)responseHandler;

        //Anel type RequestHandler & Anel responseHandler
        RequestHandle anelHandler =  client.get(url, new AsyncHttpResponseHandler() {
            // Need to transform to AAH response callback. Here we call original callback code
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                originResponseHandler.onSuccess(statusCode, headers, responseBody);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
               originResponseHandler.onFailure(statusCode, headers, responseBody, error);

            }
        });

        //Anel type AsyncHttpRequest
        //AsyncHttpRequest anelRequest = anelHandler.getRequest();

        //transform Anel AsyncHttpRequest to AAH AsyncHttpRequest
        //The relation between HttpRequest and ResponseHandle is: HttpRequest can trigger responseHandler.sendFailureMessage
        //which send Looper message, Loop calls onFailure() callback.
        //We do not need to *modify* the original responseHandler, because the stuff should be done in the callback
        //is already internally in Anel(including response validity check and show error message). If the original
        //callback has no conflict with internal checking, we just directly execute it.
        //AnelAAHAsyncHttpRequest anelaahRequest = new AnelAAHAsyncHttpRequest(anelRequest.getClient(),
         //       anelRequest.getContext(), anelRequest.getRequest(), responseHandler);

        //requestHandler is just used to cancel a request. Do not implement it for now.
        return null;
    }

}
