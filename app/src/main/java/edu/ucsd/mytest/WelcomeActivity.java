package edu.ucsd.mytest;

import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.View;
import android.widget.Button;

import edu.ucsd.mytest.service.ConnectionChangeReceiver;
import edu.ucsd.mytest.service.NetworkService;


public class WelcomeActivity extends ActionBarActivity {
    private Button volleyButton;
    private Button rawButton;
    private Button aahButton;
    private Button okButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
       // volleyButton=(Button)findViewById(R.id.volley);
        //rawButton=(Button)findViewById(R.id.raw);
        aahButton=(Button)findViewById(R.id.aah);
        okButton = (Button)findViewById(R.id.okhttp);

        Intent i = new Intent(this, NetworkService.class);
        startService(i);

        registerReceiver(new ConnectionChangeReceiver(),
            new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));

    }

//    public void startBasicNetworking(View view) {
//        Intent i = new Intent(this, BasicActivity.class);
//        startActivity(i);
//    }
//
//    public void startApacheNetworking(View view) {
//      // Intent i = new Intent(this, ApacheActivity.class);
//       //startActivity(i);
//    }
//
//
//    public void startVolleyNetworking(View view) {
//        Intent i = new Intent(this, VolleyActivity.class);
//        startActivity(i);
//    }
//
//    public void startAnotherVolleyNetworking(View view) {
//        Intent i = new Intent(this, AnotherVolleyActivity.class);
//        startActivity(i);
//    }

    public void startAAHNetworking(View view) {
       Intent i = new Intent(this, AAHActivity.class);
       startActivity(i);
    }

    public void startOKNetworking(View view) {
       Intent i = new Intent(this, OkHttpActivity.class);
       startActivity(i);
    }

    public void startAnelOkNetworking(View view) {
        Intent i = new Intent(this, AnelOKActivity.class);
        startActivity(i);
    }

//    public void startRetrofitNetworking(View view) {
//        //Intent i = new Intent(this, RetrofitActivity.class);
//        //startActivity(i);
//    }
//
//    public void startLoader(View view) {
//        Intent i = new Intent(this, LoaderActivity.class);
//        startActivity(i);
//    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //Inflate the menu; this adds items to the action bar if it is present.
       // getMenuInflater().inflate(R.menu., menu);
        return true;
    }


}

