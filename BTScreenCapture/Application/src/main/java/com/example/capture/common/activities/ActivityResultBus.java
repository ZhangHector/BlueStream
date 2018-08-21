package com.example.capture.common.activities;

import android.os.Handler;
import android.os.Looper;

import com.squareup.otto.Bus;

/**
 * Created by Ziqiao Charlie Li on 10/21/2015
 * <p/>
 * ActivityResultBus
 * <p/>
 * Manages intent sending: will be used to send a package
 * from an Activity to all of active Fragments.
 */


public class ActivityResultBus extends Bus {

    private static ActivityResultBus instance;

    public static ActivityResultBus getInstance() {
        if (instance == null)
            instance = new ActivityResultBus();
        return instance;
    }

    private Handler mHandler = new Handler(Looper.getMainLooper());

    public void postQueue(final Object obj) {
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                ActivityResultBus.getInstance().post(obj);
            }
        });
    }

}
