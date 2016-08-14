package edu.ucsd.mytest;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.widget.TextView;

import com.loopj.android.http.anel_okhttp.AnelOkHttpSyncClient;

import java.io.IOException;

import okhttp3.Request;
import okhttp3.Response;

public class AnelOKActivity extends ActionBarActivity {
    final String TAG = "ANELOK";
    TextView tv;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_anel_ok);
        tv = (TextView) findViewById(R.id.anelok_text);


        Thread t = new Thread(new FetchItemsThread());
        t.start();

    }



    private class FetchItemsThread implements Runnable {

        @Override
        public void run() {
            String url="http://www.google.com";
            try {

                AnelOkHttpSyncClient client = new AnelOkHttpSyncClient();

                Request request = new Request.Builder().url(url).build(); // Okhttp Request

                Response response= client.newCallV2(request);
                
                final String result = response.body().string();

                if (result == null)
                    Log.d(TAG, "result is null " );
                else {
                    Log.d(TAG, "Has result : " + result);
                }

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        tv.setText(result);
                    }
                });

            } catch (IOException e) {
                e.printStackTrace();
            }


        }
    }

}
