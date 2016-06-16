package edu.ucsd.mytest;

import android.content.AsyncTaskLoader;
import android.content.Context;
import android.util.Log;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpParams;

import java.io.IOException;

/**
 * Created by xinxin on 2/27/15.
 */
public class SimpleLoader extends AsyncTaskLoader<String> {
    public SimpleLoader(Context context) {
        super(context);
    }
    final String TAG ="loader";

    @Override
    public String loadInBackground() {
        Log.d(TAG, "loadInBackground called");
        HttpParams httpParameters = new BasicHttpParams();
        HttpClient client = new DefaultHttpClient(httpParameters);
        HttpGet request = new HttpGet("http://www.google.com");
        HttpResponse response = null;

        try {
            response = client.execute(request);
        } catch (ClientProtocolException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return response.toString();
    }

    @Override
    public void deliverResult(String data) {
        if (isStarted()) {
            // If the Loader is in a started state, deliver the results to the
            // client. The superclass method does this for us.
            super.deliverResult(data);
            Log.d(TAG, "deliverResult called");
        }
    }

    @Override
    protected void onStopLoading() {
        // The Loader is in a stopped state, so we should attempt to cancel the
        // current load (if there is one).
        cancelLoad();

        // Note that we leave the observer as is. Loaders in a stopped state
        // should still monitor the data source for changes so that the Loader
        // will know to force a new load if it is ever started again.
    }



}
