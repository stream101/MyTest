package edu.ucsd.mytest;

import android.content.Context;

import com.loopj.android.http.AnelClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;


/**
 * Created by xinxin on 4/8/15.
 */
public class AAHHelper {
    Context context;
    private AnelClient client;

    public AAHHelper(Context context) {
        this.context = context;
        client = new AnelClient(this.context);
    }

    public void get(String url, AsyncHttpResponseHandler responseHandler) {
        //client.setTimeout(10);
        //client.setMaxRetriesAndTimeout(2, 10);
        client.get(url, responseHandler);
    }

    public void post(String url, RequestParams params, AsyncHttpResponseHandler responseHandler) {
        client.post(url, params, responseHandler);
    }

}

/**
expected output:
public class _AAHHelper {
 private static AsyncHttpClient client;

 public AAHHelper(){
    client = new AsyncHttpClient();
    client.setTimeout(5);
    client.setMaxRetriesAndTimeout(5, 0);
 }

 public static void get() {
  //the same
 }

 public static void post() {
    //the same
 }
 }
 */