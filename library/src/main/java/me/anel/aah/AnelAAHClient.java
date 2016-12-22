package me.anel.aah;

import com.loopj.android.http.AsyncHttpClient;

import me.anel.AnelClient;

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


//    @Override
//    public com.loopj.android.http.RequestHandle get(String url, com.loopj.android.http.AsyncHttpResponseHandler responseHandler) {
//
//
//        AsyncHttpResponseHandler anelResponseHandler = new AsyncHttpResponseHandler() {
//            @Override
//            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
//                responseHandler.get
//            }
//
//            @Override
//            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
//
//            }
//        };
//
//        //Anel type RequestHanle
//        me.anel.RequestHandle anelHandle =  client.get(url, anelResponseHandler);
//        //Anel type AsyncHttpRequest
//        AsyncHttpRequest anelRequest = anelHandle.getRequest();
//
//
//        AnelAAHAsyncHttpRequest anelaahRequest = new AnelAAHAsyncHttpRequest();
//
//        com.loopj.android.http.RequestHandle  resp = new me.loopj.android.anel.aah.AnelAAHRequestHandle(request);
//        return resp;
//    }


}
