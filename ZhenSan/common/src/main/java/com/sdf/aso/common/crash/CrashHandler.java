package com.sdf.aso.common.crash;

import android.content.Context;
import android.util.Log;

import java.lang.Thread.UncaughtExceptionHandler;

/**
 * Created by itsdf07 on 2017/3/11 15:49.
 * E-Mail: 923255742@qq.com
 * GitHub: https://github.com/itsdf07
 */

public class CrashHandler implements UncaughtExceptionHandler {
    private static final String TAG = CrashHandler.class.getSimpleName();
    private static CrashHandler instance = null;


    public static synchronized CrashHandler getInstance() {
        if (instance == null) {
            instance = new CrashHandler();
        }
        return instance;
    }

    public void init(Context context) {
        Thread.setDefaultUncaughtExceptionHandler(this);
    }

    @Override
    public void uncaughtException(Thread thread, Throwable ex) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Thread:");
        stringBuilder.append(thread.toString());
        stringBuilder.append("\t");
        stringBuilder.append(ex);
        Log.i("sdf", "uncaughtException : ex === " + stringBuilder.toString());
    }
}
