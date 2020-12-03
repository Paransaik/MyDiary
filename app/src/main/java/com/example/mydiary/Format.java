package com.example.mydiary;

import android.os.Handler;
import android.util.Log;

import java.text.SimpleDateFormat;

public class Format {
    public static final int MODE_INSERT = 1;
    public static final int MODE_MODIFY = 2;

    public static SimpleDateFormat dateFormat1 = new SimpleDateFormat("MM-dd");
    public static SimpleDateFormat dateFormat2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    private static Handler handler = new Handler();
    private static final String TAG = "Format";
    public static void println(final String data) {
        handler.post(new Runnable() {
            @Override
            public void run() {
                Log.d(TAG, data);
            }
        });
    }
}
