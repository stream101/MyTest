package edu.ucsd.mytest;

import android.app.AlertDialog;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.widget.TextView;

import java.io.IOException;

import edu.ucsd.myannotation.SetReq;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class OkHttpActivity extends ActionBarActivity {
    final String TAG="okhttp";
    TextView tv;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ok_http);
         tv = (TextView)findViewById(R.id.okhttp_text);

        Thread t = new Thread(new FetchItemsThread());
        t.start();

    }

    void alertDialog () {
        new AlertDialog.Builder(this).setTitle("Error").setMessage("Watch out!").show();
    }


    private class FetchItemsThread implements Runnable {
        @SetReq(timeout=30, retry = 2)
        OkHttpClient client = new OkHttpClient();

        @Override
        public void run() {
            String url="http://www.google.com";
            Response response = null;

           // GetExample example = new GetExample();
            try {

                Request request = new Request.Builder().url(url).build();

                response= client.newCall(request).execute();
                final String result = response.body().string();
                if (result == null)
                    Log.d(TAG, "result is null " );
                else {
                    Log.d(TAG, "Has result : " + result);

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            tv.setText(result);
                        }
                    });
                }
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
