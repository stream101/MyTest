package edu.ucsd.mytest;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.widget.TextView;

import org.apache.http.Header;

import me.anel.AnelClient;
import me.anel.AsyncHttpResponseHandler;

public class AnelActivity extends ActionBarActivity {
    final String TAG="AAHActivity";
    final String URL= "http://www.google.com";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_anel);
        final TextView tv=(TextView)findViewById(R.id.anel_text);
        AnelClient client = new AnelClient();

        client.get(URL, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                tv.setText(new String(responseBody));
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                //internally show toast error message
            }
        });
    }

}
