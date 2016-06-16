package edu.ucsd.mytest;

import com.android.volley.toolbox.StringRequest;

/**
 * Created by xinxin on 4/21/15.
 */
public class VolleyRequest {
    private StringRequest mRequest;
    private VolleyClient mRestClient;

    public VolleyRequest(StringRequest request, VolleyClient restClient) {
        mRequest = request;
        mRestClient = restClient;
    }

    public void send() {
        mRestClient.send(mRequest);
    }

}
