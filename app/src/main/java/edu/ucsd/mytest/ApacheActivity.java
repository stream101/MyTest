package edu.ucsd.mytest;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.widget.TextView;

import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;

import java.io.IOException;

public class ApacheActivity extends ActionBarActivity {
	private final String url = "http://www.google.com";
    private final String TAG =  "apache";
	// Create the text view
	TextView  mTextView;
    private int DEFAULT_TIMEOUT = 30000;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mTextView = new TextView(this);
		//new FetchItemsTask().execute();
        Thread thread = new Thread(new FetchItemsTask());
        thread.start();
		
	}

    private class FetchItemsTask implements Runnable {
        private final Handler mHandler = new Handler(Looper.getMainLooper()) {
            public void handleMessage(Message msg) {
                 onPostExecute(msg.what);
            }
        };

        @Override
        public void run() {
            HttpResponse response = null;
            String result="";
            final int MAX_ATTEMPTS = 5;

            long start = System.currentTimeMillis();
            int i =0;
            int success = 0;
            while (success == 0 && i < 5) {
                start = System.currentTimeMillis();
                success = helper();
                long end = System.currentTimeMillis();
                i++;
            }

            if (response != null) {
                try {
                    processResult(response);
                } catch (ParseException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }


            long end = System.currentTimeMillis();
            Log.i(TAG, "Feteched contents of URL "+result);
            Log.i(TAG, "Elapsed time "+ (end-start)/1000);

            mHandler.sendEmptyMessage(0);

            return ;

        }

        protected void onPostExecute(int result) {
            if (result == 1)
                mTextView.setText("success");
            else
                mTextView.setText("failure");
        }
    }
	
	/*private class FetchItemsTask extends AsyncTask<Void,Void,String>{

		private static final String TAG = "xinxin";
        //int DEFAULT_TIMEOUT = 30000;

		@Override
		protected String doInBackground(Void... arg0) {
            HttpResponse response = null;
			String result="";
			final int MAX_ATTEMPTS = 5;

			long start = System.currentTimeMillis();
            int i =0;
            int success = 0;
            while (success == 0) {
                    start = System.currentTimeMillis();
                    success = helper();
                    long end = System.currentTimeMillis();
                    i++;
            }

			if (response != null) {
				try {
                    processResult(response);
				} catch (ParseException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}


			long end = System.currentTimeMillis();
			Log.i(TAG, "Feteched contents of URL "+result);
			Log.i(TAG, "Elapsed time "+ (end-start)/1000);
			return result;
		}
		

	}*/

    int helper()  {
        HttpParams httpParameters = new BasicHttpParams();
        int timeoutConnection = DEFAULT_TIMEOUT;
        HttpConnectionParams.setConnectionTimeout(httpParameters, timeoutConnection);
        int timeoutSocket = DEFAULT_TIMEOUT;
        HttpConnectionParams.setSoTimeout(httpParameters, timeoutSocket);

        DefaultHttpClient client = new DefaultHttpClient(httpParameters);
        //DefaultHttpClient client = new DefaultHttpClient();
        HttpGet request = new HttpGet(url);
        HttpHost host = new HttpHost(url,8080);

        HttpResponse response = null;
        int success = 1;
        try {
            ResponseHandler<String> responseHandler = new ResponseHandler<String>() {

                @Override
                public String handleResponse(HttpResponse response) throws ClientProtocolException, IOException {
                    int status = response.getStatusLine().getStatusCode();
                    if (status >= 200 && status < 300) {
                        HttpEntity entity = response.getEntity();
                        return entity != null ? EntityUtils.toString(entity) : null;
                    } else {
                        throw new ClientProtocolException("Unexpected response status: " + status);
                    }
                }
            };


            response = client.execute(host, request);
            Log.d(TAG, "respone " + response);
        } catch (IOException e) {
            success = 0;
        }
        return success;
    }

    String processResult(HttpResponse response) throws IOException {
        return print(response);
    }

    String print(HttpResponse response) throws IOException {
        String result = EntityUtils.toString(response.getEntity());
        Log.d(TAG, result);
        return result;
    }

}
