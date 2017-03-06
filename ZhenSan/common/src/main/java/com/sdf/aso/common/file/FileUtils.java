package com.sdf.aso.common.file;

import android.content.Context;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;

/**
 * Created by itsdf07 on 2017/3/6 14:56.
 * E-Mail: 923255742@qq.com
 * GitHub: https://github.com/itsdf07
 */

public class FileUtils {
    private static final String TAG = FileUtils.class.getSimpleName();

    public static String getRawResource(Context context, int raw) {
        InputStream inputStream = context.getResources().openRawResource(raw);
        return getString(inputStream);
    }

    private static String getString(InputStream inputStream) {
        InputStreamReader inputStreamReader = null;
        try {
            inputStreamReader = new InputStreamReader(inputStream, "utf-8");
        } catch (UnsupportedEncodingException e1) {
            e1.printStackTrace();
        }
        BufferedReader reader = new BufferedReader(inputStreamReader);
        StringBuffer sb = new StringBuffer("");
        String line;
        try {
            while ((line = reader.readLine()) != null) {
                sb.append(line);
                sb.append("\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        Log.d(TAG, " getString : " + sb.toString());
        return sb.toString();
    }
}
