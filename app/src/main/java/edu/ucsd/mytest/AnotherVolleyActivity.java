package edu.ucsd.mytest;

import android.content.Context;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;

public class AnotherVolleyActivity extends ActionBarActivity {

    final String TAG = "test";
    String baseURL ="http://xinxin.ucsd.edu/test_files/";
    String files[] = {"2k", "4k", "8k", "16k", "32k",
            "64k", "128k", "256k", "512k", "1024k",
            "2048k" /*, "4096k"*/};
    final int N = 1;
    WifiManager wifiManager;
    WifiManager.WifiLock wifiLock;
    HttpHelper client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    protected void onResume() {
        super.onResume();

        int count_down = files.length * N;

        Log.d(TAG, "external storage writable ? " + isExternalStorageWritable());


        client = HttpHelper.getInstance(this, count_down);
        if (client.isConnected()) {
            wifiManager = (WifiManager) this.getSystemService(Context.WIFI_SERVICE);
            wifiLock = wifiManager.createWifiLock("WakeLockPermissionTest");
            wifiLock.acquire(); //don't release
            new FetchItemsTask().execute();
        }
    }

    private class FetchItemsTask extends AsyncTask<Void,Void,String> {
        @Override
        protected String doInBackground(Void... params) {
            //for(int i =0; i<N; i++) {
                for (int l=files.length - 1 ; l>=0; l--) {
                    String subURL = files[l];
                    String url = baseURL + subURL;
                    for(int i =0; i<N; i++) {
                        client.get(url, null);
                        try {
                            Thread.sleep(10000); //debug
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
            //}

           return null;
        }
    }


    /* Checks if external storage is available for read and write */
    public boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            return true;
        }
        return false;
    }
}
