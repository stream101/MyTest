package edu.ucsd.mytest;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import edu.ucsd.SetReq;


/**
 * Created by xinxin on 4/8/15.
 */
@SetReq(timeout=5, retry=5)
public class AAHHelper {

    private static AsyncHttpClient client = new AsyncHttpClient();


    public static void get(String url, AsyncHttpResponseHandler responseHandler) {
        //client.setTimeout(10);
        //client.setMaxRetriesAndTimeout(2, 10);
        client.get(url, responseHandler);
    }

    public static void post(String url, RequestParams params, AsyncHttpResponseHandler responseHandler) {
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