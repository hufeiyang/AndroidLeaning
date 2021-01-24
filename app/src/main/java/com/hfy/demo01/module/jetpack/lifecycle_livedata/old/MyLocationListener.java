package com.hfy.demo01.module.jetpack.lifecycle_livedata.old;

import android.content.Context;
import android.util.Log;

public class MyLocationListener {
    private static final String TAG = "MyLocationListener";

    public MyLocationListener(Context context, LocationCallback callback) {
        // ...
    }

    void start() {
        // connect to system location service

        Log.i(TAG, "start: ");
    }

    void stop() {
        // disconnect from system location service
        Log.i(TAG, "stop: ");

    }
}
