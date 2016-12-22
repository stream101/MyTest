package edu.ucsd.mytest;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.widget.TextView;

import com.loopj.android.http.AsyncHttpClient;

import me.anel.aah.AnelAAHClient;

public class AnelAAHActivity extends ActionBarActivity {

    final String TAG="AAHActivity";
    final String URL= "http://www.google.com";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_anel_aah);
        final TextView tv=(TextView)findViewById(R.id.anelaah_text);
        AsyncHttpClient client = new AnelAAHClient();

        client.setTimeout(30000);

//        client.get(URL, new AsyncHttpResponseHandler(this) {
//            @Override
//            public void onSuccess(int i, Header[] headers, byte[] bytes) {
//                //Log.d(TAG, "response " + new String(bytes));
//                tv.setText(new String(bytes));
//            }
//
//            @Override
//            public void onFailure(int statusCode, Header[] headers, byte[] errorResponse, Throwable throwable) {
//                //Log.d(TAG, "fail: " + "status: " + statusCode + " throw " + throwable);
//                if (errorResponse != null)
//                    Log.d(TAG, "error response " + new String(errorResponse));
//                tv.setText("aah failure");
//            }
//
//        });
    }

}
