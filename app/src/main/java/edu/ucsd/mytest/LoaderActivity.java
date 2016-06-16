package edu.ucsd.mytest;

import android.app.LoaderManager;
import android.content.Loader;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;


public class LoaderActivity extends ActionBarActivity implements LoaderManager.LoaderCallbacks {
    final String TAG =  "loader";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loader);
        Log.d(TAG, "onCreate() called ");
        getLoaderManager().initLoader(0, null, this).forceLoad();
    }

    @Override
    public Loader onCreateLoader(int id, Bundle args) {
        Log.d(TAG, "onCreateLoader() called ");
        return new SimpleLoader(this);
    }

    @Override
    public void onLoadFinished(Loader loader, Object data) {
        Log.d(TAG, "onLoadFinished() called ");
        Log.d(TAG, "received " + data.toString());
    }

    @Override
    public void onLoaderReset(Loader loader) {

    }
}
