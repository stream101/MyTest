package edu.ucsd.mytest;

import android.content.BroadcastReceiver;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.NoConnectionError;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;

import edu.ucsd.mytest.service.ConnectionChangeReceiver;

public class VolleyActivity extends ActionBarActivity {

    final String TAG = "volley";
    long start, end;
    BroadcastReceiver receiver;

    void alertError(String s) {
        Toast.makeText(this, s, Toast.LENGTH_LONG).show();
    }

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        receiver = new ConnectionChangeReceiver();
        registerReceiver(receiver,
                    new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));


        //If SDK_INT >= 9, use hurl stack; otherwise use httpclient
		RequestQueue queue = Volley.newRequestQueue(this);
		String url ="http://www.google.com";
        VolleyClientUtils util = new VolleyClientUtils(queue);

        util.post(url, null, new Response.Listener() {
            /* Here, if response = null, not caused by network error */
            @Override
            public void onResponse(Object response) {
                //mTextView.setText("Response is: "+ ((String) response).substring(0,500));
                //if (response != null)
                Log.d(TAG, "response " + ((String) response).substring(0,500));
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                // Show timeout error message
                if (error instanceof TimeoutError)
                    alertError("Oops. Timeout error!");
                else if (error instanceof ServerError)
                    alertError("Oops. Server error!");
                else if (error instanceof NoConnectionError)
                    alertError("Oops. No Network!");

                end = System.nanoTime();
                Log.d(TAG, "Error return in "+ (end-start)/1000000 + " ms");
                Log.d(TAG, "error message ");
            }
        });



        //Android uses okhttp powered HttpURLConnection by default
        //If I set timeout value as 2 s and retry count as 0, then if it
        //fails, it should return by more than 2 s
        //Answer: yes it returns in 10s
       // request.setRetryPolicy(new DefaultRetryPolicy(3000,1,1));
        //start = System.nanoTime();
	    //queue.add(request);

	    //setContentView(textView);
	}

    @Override
    protected void onStop() {
        super.onStop();
        unregisterReceiver(receiver);

    }
}
