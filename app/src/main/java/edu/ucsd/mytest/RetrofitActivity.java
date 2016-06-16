package edu.ucsd.mytest;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.widget.TextView;

import retrofit.ErrorHandler;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.http.GET;

public class RetrofitActivity extends ActionBarActivity {
    final String TAG = "retrofit";

    interface SimpleAPI {
        @GET("/")
        String simpleGet();
    }

    @Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		final String url ="http://www.google.com";
		// Create the text view
		final TextView  mTextView = new TextView(this);
	   // textView.setTextSize(40)
		RestAdapter restAdapter = new RestAdapter.
                                      Builder().
                                      setEndpoint(url).
                                      setErrorHandler(new MyErrorHandler()).
                                      build();
		
	    SimpleAPI api = restAdapter.create(SimpleAPI.class);
      //  String response = api.simpleGet();
      //  Log.d(TAG, "get response " + response);
	}

    private class MyErrorHandler implements ErrorHandler {

        @Override
        public Throwable handleError(RetrofitError retrofitError) {
            Log.d(TAG, retrofitError.getResponse().toString());
            return null;
        }
    }
}
