package edu.ucsd.mytest;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.turbomanage.httpclient.AsyncCallback;
import com.turbomanage.httpclient.BasicHttpClient;
import com.turbomanage.httpclient.HttpResponse;
import com.turbomanage.httpclient.ParameterMap;
import com.turbomanage.httpclient.android.AndroidHttpClient;

import java.net.HttpURLConnection;


public class BasicActivity extends ActionBarActivity {
    final String TAG = "basic";
    final int RETRIES = 5;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       setContentView(R.layout.activity_basic);

        AndroidHttpClient httpClient = new AndroidHttpClient("http://www.google.com");
        setClient(httpClient);
        ParameterMap params = httpClient.newParams()
                .add("continue", "/")
                .add("email", "test@example.com")
                .add("action", "Log In");
        httpClient.post("/_ah/login", params, new AsyncCallback() {

            @Override
            public void onComplete(HttpResponse httpResponse) {

                int status = httpResponse.getStatus();
                String s = httpResponse.getBodyAsString();
                Log.d("TAG", "response = " + s);
            }

            @Override
            public void onError(Exception e) {

                Toast.makeText(getApplicationContext(), "basic http error", Toast.LENGTH_LONG).show();
            }
        });

        BasicHttpClient basicClient = new BasicHttpClient();
        HttpResponse response = basicClient.get("http://www.google.com", null);
      //  response.getBodyAsString();

        if (response == null) {
            Log.d(TAG, "response is null");
            //throw new IOException("Request for data manifest returned null response.");
        } else {

            int status = response.getStatus();
            if (status == HttpURLConnection.HTTP_OK) {
                String body = response.getBodyAsString();
                if (TextUtils.isEmpty(body)) {

                }
            }
        }

    }

    void setClient(AndroidHttpClient httpClient) {
        httpClient.setConnectionTimeout(1);
        httpClient.setReadTimeout(1);
        httpClient.setMaxRetries(RETRIES);
    }
}
