package com.loopj.android.http;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import java.util.ArrayList;
import java.util.concurrent.ExecutorService;

import static android.net.ConnectivityManager.CONNECTIVITY_ACTION;

/**
 * Created by xinxin on 6/26/16.
 */

public class Dispatcher {
    Context context;
    private ExecutorService threadPool;
    static volatile Dispatcher singleton = null;
    final NetworkChangeReceiver receiver;
    //final Handler handler;
    //final DispatcherThread dispatcherThread;
    private static final String DISPATCHER_THREAD_NAME = "Dispatcher";
    static private ArrayList<AsyncHttpRequest> failedRequests = new ArrayList<AsyncHttpRequest>();
    static final String LOG_TAG = "Dispatcher";


    Dispatcher(Context context, ExecutorService threadPool) {
        this.context = context;
        this.threadPool = threadPool;
        // this.dispatcherThread = new DispatcherThread();
        //this.dispatcherThread.start();
        this.receiver = new NetworkChangeReceiver(this);
        receiver.register();
       // this.handler  = new DispatcherHandler(this.dispatcherThread.getLooper(), this);
    }

    static public void saveFailedRequest(AsyncHttpRequest request) {
        failedRequests.add(request);
        Log.d(LOG_TAG, "save failed req " + request);
    }

     void flushFailedRequest() {
        if (!failedRequests.isEmpty()) {
            for (AsyncHttpRequest request: failedRequests) {
                failedRequests.remove(request);
                Log.d(LOG_TAG, "flush failed req " + request);
                threadPool.submit(request);
            }
        }
    }


    void shutdown() {
        this.receiver.unregister();
    }

   /* void dispatchRetry() {
     //   handler.sendMessageDelayed(handler.obtainMessage(), RETRY_DEPLAY);
    //}


    private static class DispatcherHandler extends Handler {
        private final Dispatcher dispatcher;

        public DispatcherHandler(Looper looper, Dispatcher dispatcher) {
            super(looper);
            this.dispatcher = dispatcher;
        }

       @Override
        public void handleMessage(final Message msg) {
            switch(msg.what) {
                case REQUEST_RETRY:
                    dispatcher.performRetry();
                    break;
                default:
                    break;
            }
       }
    }


     static class DispatcherThread extends HandlerThread {
        DispatcherThread() {
            super(DISPATCHER_THREAD_NAME);
        }

    }*/

    static class NetworkChangeReceiver extends BroadcastReceiver {
        private final Dispatcher dispatcher;

        NetworkChangeReceiver(Dispatcher d) {
            dispatcher = d;
        }


        void register() {
            dispatcher.context.registerReceiver(this, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
        }

        void unregister() {
            //TBD: do we need to unregister in main thread?
            dispatcher.context.unregisterReceiver(this);
        }

        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent == null) {
                return;
            }
            final String action = intent.getAction();
            if (CONNECTIVITY_ACTION.equals(action)) {
                Log.d(LOG_TAG, "receive connection broadcast");
                ConnectivityManager cm = (ConnectivityManager)
                        context.getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo activeNetwork = cm.getActiveNetworkInfo();

                boolean isConnected = activeNetwork != null &&
                        activeNetwork.isConnectedOrConnecting();

                if (isConnected)
                    dispatcher.flushFailedRequest();

            }

        }
    }



}
