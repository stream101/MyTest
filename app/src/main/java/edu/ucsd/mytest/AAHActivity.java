package edu.ucsd.mytest;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.widget.TextView;

import org.apache.http.Header;

import me.anel.AsyncHttpResponseHandler;

public class AAHActivity extends ActionBarActivity {

    final String TAG="AAHActivity";
    final String URL= "http://www.google.com";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aahnetworking);
        final TextView tv=(TextView)findViewById(R.id.aah_text);
        AAHHelper httpHelper = new AAHHelper(getApplicationContext());

       httpHelper.get(URL, new AsyncHttpResponseHandler(this) {
           @Override
           public void onSuccess(int i, Header[] headers, byte[] bytes) {
               //Log.d(TAG, "response " + new String(bytes));
               tv.setText(new String(bytes));
           }

           @Override
           public void onFailure(int statusCode, Header[] headers, byte[] errorResponse, Throwable throwable) {
               //Log.d(TAG, "fail: " + "status: " + statusCode + " throw " + throwable);
               if (errorResponse != null)
                  Log.d(TAG, "error response " + new String(errorResponse));
               tv.setText("aah failure");
           }

       });


      /* AAHHelper.post(URL, null, new AsyncHttpResponseHandler() {
           @Override
           public void onSuccess(int i, Header[] headers, byte[] bytes) {
           }

           @Override
           public void onFailure(int i, Header[] headers, byte[] bytes, Throwable throwable) {
           }
       });*/



    }


}
