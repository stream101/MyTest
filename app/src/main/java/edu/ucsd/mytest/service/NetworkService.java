package edu.ucsd.mytest.service;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

public class NetworkService extends IntentService {
    final String TAG = "service";

    public NetworkService() {
        super("NetworkService");
    }


    @Override
    protected void onHandleIntent(Intent intent) {
        int len = 500;
        InputStream is = null;
        int items = 10;

        for (int i = 0; i < items; i++) {
            try {
                URL url = new URL("http://www.google.com");
                HttpURLConnection conn1 = createNewInstance(url);
                HttpURLConnection conn = conn1;
                setConn(conn);
                //conn.setReadTimeout(1000 /* milliseconds */);
                //conn.setConnectTimeout(1000 /* milliseconds */);
                //conn.setRequestMethod("GET");
                //conn.setDoInput(true);
                int response = conn.getResponseCode();
                Log.d(TAG, "The response is: " + response);
                is = conn.getInputStream();

                // Convert the InputStream into a string
                String contentAsString = readIt(is, len);
                Log.d(TAG, "service get url " + contentAsString);

            } catch (MalformedURLException e) {

            } catch (ProtocolException e) {

            } catch (IOException e) {
                //when timeout, okhttp throws socketTimeOut exception
                e.printStackTrace();

            }
        }
    }

    HttpURLConnection createNewInstance(URL url) {
        try {
            return (HttpURLConnection) url.openConnection();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    void setConn(HttpURLConnection conn) {


    conn.setReadTimeout(1000 /* milliseconds */);
    conn.setConnectTimeout(1000 /* milliseconds */);
        try {
            conn.setRequestMethod("GET");
        } catch (ProtocolException e) {
            e.printStackTrace();
        }
        conn.setDoInput(true);
}


    // Reads an InputStream and converts it to a String.
    public String readIt(InputStream stream, int len) throws IOException, UnsupportedEncodingException {
        Reader reader = null;
        reader = new InputStreamReader(stream, "UTF-8");
        char[] buffer = new char[len];
        reader.read(buffer);
        return new String(buffer);
    }
}
