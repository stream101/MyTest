package edu.ucsd.mytest;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.http.AndroidHttpClient;
import android.os.Environment;
import android.util.Log;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpClientStack;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.io.File;
import java.io.FileOutputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by xinxin on 1/21/15.
 */
public class HttpHelper {
    private RequestQueue mRequestQueue;
    private static Context mContext;
    private static HttpHelper mInstance;
    public static final String TAG = "test";
    public HashMap<String, Integer> total = new HashMap<String, Integer>();
    public HashMap<String, Integer> Succ = new HashMap<String, Integer>();
    public HashMap<String, Integer> Fail = new HashMap<String, Integer>();
    private final String FileName = "network.txt";
    private File file;
    //private int count_down;
    private AtomicInteger count_down;

    public static final int REST_TIMEOUT_MS = 30000;
    public static final int REST_MAX_RETRIES_POST = 0;
    public static final int REST_MAX_RETRIES_GET = 3;
    public static final float REST_BACKOFF_MULT = 2f;




    @SuppressWarnings("static-access")
    private HttpHelper(Context context, int c) {
        this.mContext = context;
        mRequestQueue = getRequestQueue();
        this.count_down = new AtomicInteger(c);
        File sdCard = Environment.getExternalStorageDirectory();
        File dir = new File (sdCard.getAbsolutePath() + "/MyNetworkTest");
        dir.mkdirs();
        file = new File(dir, FileName);
        Log.d(TAG, "Write to file " + file.getAbsolutePath());
    }

    public static synchronized HttpHelper getInstance(Context context, int count_down) {
        if (mInstance == null) {
            mInstance = new HttpHelper(context, count_down);
        }
        return mInstance;
    }

    public boolean isConnected() {
        ConnectivityManager cm =
                (ConnectivityManager)this.mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();
        return isConnected;
    }


    public RequestQueue getRequestQueue() {
        if (mRequestQueue == null) {
            //Use apache stack to eliminate the impact of okhttp in hurl
            mRequestQueue = Volley.newRequestQueue(mContext
                    .getApplicationContext(), new HttpClientStack(AndroidHttpClient.newInstance("volley/0")));
        }
        return mRequestQueue;
    }

    public <T> void addToRequestQueue(Request<T> req) {
        getRequestQueue().add(req);
    }

    private void addTotalReq(String url) {
        if (!total.containsKey(url))
            total.put(url, new Integer(1));
        else
            total.put(url, new Integer(total.get(url).intValue() + 1));
    }


    private void addSuccReq(String url) {
        if (!Succ.containsKey(url)) {
            Succ.put(url, new Integer(1));
        }
        else {
            Succ.put(url, new Integer(Succ.get(url).intValue() + 1));
        }

        int d = count_down.decrementAndGet();
        Log.d(TAG, "count down " + d);

    }


    private void addFailReq(String url) {
       /* if (!Fail.containsKey(url))
            Fail.put(url, new Integer(1));
        else
            Fail.put(url, new Integer(Fail.get(url).intValue() + 1));*/
        int d = count_down.decrementAndGet();
       Log.d(TAG, "count down " + d);
    }


    public void get(final String url, RetryPolicy retryPolicy) {
        addTotalReq(url);

        StringRequest topicRequest = new StringRequest(url,

                new Response.Listener() {
                    /* Here, if response = null, not caused by network error */
                    @Override
                    public void onResponse(Object response) {
                      if (response != null) {
                          //Log.d(TAG,"SUCC");
                          addSuccReq(url);
                          printResult();
                      }
                    }
                },

                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                       Log.d(TAG, url + " FAIL "  + error);

                       addFailReq(url);
                       printResult();
                    }
                });

        if (retryPolicy == null)
            topicRequest.setRetryPolicy(new DefaultRetryPolicy());
        else
            topicRequest.setRetryPolicy(retryPolicy);

        addToRequestQueue(topicRequest);
    }

    void printResult() {
        if (count_down.get() != 0) {
            return;
        }

       /* Log.d(TAG, "Total Request: " );
        for (Map.Entry<String, Integer> entry : total.entrySet()) {
            String url = entry.getKey();
            Integer num = entry.getValue();
            Log.d(TAG, url + " , " + num);
        }*/

        /*Log.d(TAG, "Total Successful Request: " );
        for (Map.Entry<String, Integer> suc : Succ.entrySet()) {
            String url = suc.getKey();
            Integer num = suc.getValue();
            Log.d(TAG, url + " , " + num);
        }*/
        //Write to file /storage/emulated/0/MyNetworkTest/network.txt
        try {
            FileOutputStream outputStream = new FileOutputStream(file);
            for (Map.Entry<String, Integer> suc : Succ.entrySet()) {
                String url = suc.getKey();
                Integer num = suc.getValue();
                String data =  url + " , " + num + "\n";
                outputStream.write(data.getBytes());
            }
            outputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
