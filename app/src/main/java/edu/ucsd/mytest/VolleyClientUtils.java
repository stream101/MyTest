package edu.ucsd.mytest;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.toolbox.StringRequest;

import static com.android.volley.Request.Method;

/**
 * Created by xinxin on 4/21/15.
 */
public class VolleyClientUtils {
    private VolleyClient client;
    public static final int REST_TIMEOUT_MS = 30000;
    public static final int REST_MAX_RETRIES_GET = 3;
    public static final int REST_MAX_RETRIES_POST = 0;
    public static final float REST_BACKOFF_MULT = 2f;

    public VolleyClientUtils(RequestQueue queue) {
        client = new VolleyClient(queue);
    }

    public StringRequest post(final String url, RetryPolicy retryPolicy, Response.Listener listener,
                Response.ErrorListener errorListener) {
        final StringRequest request = client.makeRequest(Method.POST, url, listener, errorListener);
        if (retryPolicy == null) {
            retryPolicy = new DefaultRetryPolicy(REST_TIMEOUT_MS, REST_MAX_RETRIES_POST,
                    REST_BACKOFF_MULT); //Do not retry on failure
        }
        request.setRetryPolicy(retryPolicy);
        VolleyRequest newReq = new VolleyRequest(request, client);
        newReq.send();
        return request;
    }

    public StringRequest get(final String url, RetryPolicy retryPolicy, Response.Listener listener,
                              Response.ErrorListener errorListener) {
        final StringRequest request = client.makeRequest(Method.GET, url, listener, errorListener);
        if (retryPolicy == null) {
            retryPolicy = new DefaultRetryPolicy(REST_TIMEOUT_MS, REST_MAX_RETRIES_GET,
                    REST_BACKOFF_MULT); //Do not retry on failure
        }
        request.setRetryPolicy(retryPolicy);
        VolleyRequest newReq = new VolleyRequest(request, client);
        newReq.send();
        return request;
    }

}

