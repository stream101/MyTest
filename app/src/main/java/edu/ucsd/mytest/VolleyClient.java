package edu.ucsd.mytest;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

/**
 * Created by xinxin on 4/21/15.
 */
public class VolleyClient {

    private RequestQueue mQueue;
    public static enum REST_CLIENT_VERSIONS {V1, V1_1}

    public VolleyClient(RequestQueue queue) {
        this.mQueue = queue;
    }

    public StringRequest send(StringRequest req) {
        this.mQueue.add(req);
        return req;
    }

   /* public StringRequest get(String path, Response.Listener listener, Response.ErrorListener errorListener) {
        return makeRequest(Request.Method.GET, path, listener, errorListener);
    }

    public StringRequest post(String path, Response.Listener listener, Response.ErrorListener errorListener) {
        return makeRequest(Request.Method.POST, path, listener, errorListener);
    }*/

    public StringRequest makeRequest(int method, String url, Response.Listener listener,
                                   Response.ErrorListener errorListener) {
        StringRequest request = new StringRequest(method, url, listener, errorListener);
        return request;
    }

}
