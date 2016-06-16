package edu.ucsd.mytest;

import android.app.AlertDialog;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;

import com.squareup.okhttp.Call;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.IOException;
import java.util.concurrent.TimeUnit;


public class OkHttpActivity extends ActionBarActivity {
    final String TAG="okhttp";
    final Handler mainHandler = new Handler(this.getMainLooper());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ok_http);

        String url="http://www.google.com";
        OkHttpClient client = new OkHttpClient();
        client.setConnectTimeout(0, TimeUnit.SECONDS); // connect timeout
        client.setReadTimeout(0, TimeUnit.SECONDS);    // socket timeout
        client.setRetryOnConnectionFailure(true);

        //new FetchItemsTask().execute();
        Thread t = new Thread(new FetchItemsThread());
        t.start();


        Request request = new Request.Builder().url(url).post(null).build();
        Call call = client.newCall(request);


        call.enqueue(new Callback() {
            //possible error:
            // a. no complete error message in onFailure because IOException is obscure
            // b. no timeout so no Response
            public void onFailure(Request request, IOException e) {
                Log.e(TAG, "Failed to execute " + request, e);
               // Toast.makeText(getApplicationContext(), "okhttp error", Toast.LENGTH_LONG).show();
               handleError();

               runOnUiThread(new Runnable() {

                    @Override
                    public void run() {
                        alertDialog();
                    }
               });

            }

            public void onResponse(Response response) throws IOException {
                if (!response.isSuccessful()) {
                    throw new IOException("Unexpected code " + response);
                }
                 Log.d(TAG, response.body().string());
            }
        });
    }

    void handleError() {
        mainHandler.post(new Runnable() {

            @Override
            public void run() {
                alertDialog();
            }
        });
    }


    void alertDialog () {
        new AlertDialog.Builder(this).setTitle("Error").setMessage("Watch out!").show();
    }


    private class FetchItemsThread implements Runnable {

        @Override
        public void run() {
            String url="http://www.google.com";
            String result = null;
            Response response = null;

           // GetExample example = new GetExample();
            try {
                OkHttpClient client = new OkHttpClient();
                client.setConnectTimeout(0, TimeUnit.SECONDS); // connect timeout
                client.setReadTimeout(0, TimeUnit.SECONDS);    // socket timeout

                Request request = new Request.Builder().url(url).build();

                response= client.newCall(request).execute();
                result = response.body().string();
                if (result == null)
                    Log.d(TAG, "result is null " );
                else
                    Log.d(TAG, "Has result : " + result );
            } catch (IOException e) {
                // Note: java compiler force to catch IO exception with newCall().execute(). And
                 //when there is network failure (e.g. socketTimeout, UnknownHost), it will execute
                 //the catch branch and returned result would be null.

                  //The errors can be made by developers:
                  //a. do not check null response in onPostExecute
                  //b. do not set timeout so never execute onPostexecute
                  //c. do not know the exact reason of IOException so no error message in onPostExecute

                Log.e(TAG, "network failure");
            }

           // return response;
        }

    }


}
