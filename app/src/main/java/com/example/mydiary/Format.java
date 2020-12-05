package com.example.mydiary;

import android.os.Handler;
import android.util.Log;

import java.text.SimpleDateFormat;

public class Format {
    //저장 버튼을 눌렀을 때 상태가 새로 작성인지, 수정인지 확인하는 flag
    public static final int MODE_INSERT = 1;
    public static final int MODE_MODIFY = 2;

    //날짜들의 Format 값
    public static SimpleDateFormat dateFormat1 = new SimpleDateFormat("MM-dd");
    public static SimpleDateFormat dateFormat2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    //현태 상태, 오류 출력을 위한 println에 사용하는 Handler
    private static Handler handler = new Handler();
    //현재 상태값을 출력이 어느 클레스에서 실행되는지 확인하기 위한 TAG
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
