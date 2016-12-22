package edu.ucsd.mytest;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.widget.TextView;

import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.IOException;

import me.anel.okhttp.AnelOkHttpSyncClient;

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

                String[] spec = {"UserReq", "Idempotent"};
                AnelOkHttpSyncClient client = new AnelOkHttpSyncClient(spec);

                Request request = new Request.Builder().url(url).build(); // Okhttp Request

                // method 1: bytecode rewrite old interface
                //Response response= client.newCallV2(request);

                //method2: keep original interface
                Response response=client.newCall(request).execute();


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
