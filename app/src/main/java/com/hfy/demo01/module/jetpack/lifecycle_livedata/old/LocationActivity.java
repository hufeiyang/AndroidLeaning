package com.hfy.demo01.module.jetpack.lifecycle_livedata.old;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.hfy.demo01.R;

public class LocationActivity extends AppCompatActivity {

    private MyLocationListener myLocationListener;

    public static void launch(Activity activity) {
        Intent intent = new Intent(activity, LocationActivity.class);
        activity.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location);


        myLocationListener = new MyLocationListener(this,new LocationCallback(){
            @Override
            public void location() {
                //更新UI
            }
        });
    }



    @Override
    public void onStart() {
        super.onStart();
//        myLocationListener.start();

//        Util.checkUserStatus(result -> {
//            // what if this callback is invoked AFTER activity is stopped?
//            if (result) {
//                myLocationListener.start();
//            }
//        });

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(7000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                myLocationListener.start();
            }
        }).start();

        // manage other components that need to respond
        // to the activity lifecycle
    }

    @Override
    public void onStop() {
        super.onStop();
        myLocationListener.stop();
        // manage other components that need to respond
        // to the activity lifecycle
    }
}
