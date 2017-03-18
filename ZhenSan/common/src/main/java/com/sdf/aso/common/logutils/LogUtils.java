package com.sdf.aso.common.logutils;

import android.util.Log;

import com.sdf.aso.common.file.FileUtils;
import com.sdf.aso.common.global.GlobalUtiles;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 日志相关工具类
 * Created by itsdf07 on 2017/3/14 14:06.
 * E-Mail: 923255742@qq.com
 * GitHub: https://github.com/itsdf07
 */

public class LogUtils {
    private static String TAG = GlobalUtiles.DEFAULT_TAG;
    /**
     * 日期格式
     */
    private static SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
    private static boolean IS_LOG = true;//是否在控制台打印log信息
    private static boolean IS_LOG_2FILES = true;//是否把log信息写入存储卡上


    private LogUtils() {
        throw new UnsupportedOperationException("Sorry,Instantiation [LogUtils.java] Failure!");
    }

    /**
     * 初始化Log工具类相关属性
     *
     * @param tag         过滤的TAG
     * @param isLog       是否打印Log信息
     * @param isLog2Files Log信息是否写入文件保存
     */
    public static void init(String tag, boolean isLog, boolean isLog2Files) {
        TAG = tag;
        IS_LOG = isLog;
        IS_LOG_2FILES = isLog2Files;
    }

    /**
     * 初始化Log工具类相关属性
     *
     * @param isLog       是否打印Log信息
     * @param isLog2Files Log信息是否写入文件保存
     */
    public static void init(boolean isLog, boolean isLog2Files) {
        init(TAG, isLog, isLog2Files);
    }

    /**
     * 初始化Log工具类相关属性
     *
     * @param isLog 是否打印Log信息
     */
    public static void init(boolean isLog) {
        init(TAG, isLog, false);
    }

    /**
     * Debug日志
     *
     * @param msg 消息
     */
    public static void d(Object msg) {
        d(TAG, msg);
    }

    /**
     * Debug日志
     *
     * @param tag 标签
     * @param msg 消息
     */
    public static void d(String tag, Object msg) {// 调试信息
        d(tag, msg, null);
    }

    /**
     * Debug日志
     *
     * @param tag 标签
     * @param msg 消息
     * @param tr  异常
     */
    public static void d(String tag, Object msg, Throwable tr) {
        log(tag, msg.toString(), tr, 'd');
    }

    /**
     * Error日志
     *
     * @param msg 消息
     */
    public static void e(Object msg) {
        e(TAG, msg);
    }

    /**
     * Error日志
     *
     * @param tag 标签
     * @param msg 消息
     */
    public static void e(String tag, Object msg) {
        e(tag, msg, null);
    }

    /**
     * Error日志
     *
     * @param tag 标签
     * @param msg 消息
     * @param tr  异常
     */
    public static void e(String tag, Object msg, Throwable tr) {
        log(tag, msg.toString(), tr, 'e');
    }

    /**
     * Info日志
     *
     * @param msg 消息
     */
    public static void i(Object msg) {
        i(TAG, msg);
    }

    /**
     * Info日志
     *
     * @param tag 标签
     * @param msg 消息
     */
    public static void i(String tag, Object msg) {
        i(tag, msg, null);
    }

    /**
     * Info日志
     *
     * @param tag 标签
     * @param msg 消息
     * @param tr  异常
     */
    public static void i(String tag, Object msg, Throwable tr) {
        log(tag, msg.toString(), tr, 'i');
    }

    /**
     * Verbose日志
     *
     * @param msg 消息
     */
    public static void v(Object msg) {
        v(TAG, msg);
    }

    /**
     * Verbose日志
     *
     * @param tag 标签
     * @param msg 消息
     */
    public static void v(String tag, Object msg) {
        v(tag, msg, null);
    }

    /**
     * Verbose日志
     *
     * @param tag 标签
     * @param msg 消息
     * @param tr  异常
     */
    public static void v(String tag, Object msg, Throwable tr) {
        log(tag, msg.toString(), tr, 'v');
    }


    /**
     * Warn日志
     *
     * @param msg 消息
     */
    public static void w(Object msg) {
        w(TAG, msg);
    }

    /**
     * Warn日志
     *
     * @param tag 标签
     * @param msg 消息
     */
    public static void w(String tag, Object msg) {
        w(tag, msg, null);
    }

    /**
     * Warn日志
     *
     * @param tag 标签
     * @param msg 消息
     * @param tr  异常
     */
    public static void w(String tag, Object msg, Throwable tr) {
        log(tag, msg.toString(), tr, 'w');
    }


    /**
     * 根据tag, msg和等级，输出日志
     *
     * @param tag  标签
     * @param msg  消息
     * @param tr   异常
     * @param type 日志类型:d\e\i\v\w
     */
    private static void log(String tag, String msg, Throwable tr, char type) {
        if (IS_LOG) {
            if ('d' == type) {
                Log.d(tag, msg, tr);
            } else if ('e' == type) {
                Log.e(tag, msg, tr);
            } else if ('i' == type) {
                Log.i(tag, msg, tr);
            } else if ('v' == type) {
                Log.v(tag, msg, tr);
            } else if ('w' == type) {
                Log.w(tag, msg, tr);
            } else {
                Log.e(tag, "the log type is exception ：" + msg, tr);
            }
            if (IS_LOG_2FILES) {
                logWrite2File(type, tag, msg + '\n' + Log.getStackTraceString(tr));
            }
        }
    }

    /**
     * 文件写入
     *
     * @param type    日志类型:d\e\i\v\w
     * @param tag     标签
     * @param content 内容
     **/
    private synchronized static void logWrite2File(final char type, final String tag, final String content) {
        if (content == null) {
            return;
        }
        Date now = new Date();
        String date = new SimpleDateFormat("MM-dd").format(now);
        final String logFilePath = FileUtils.INNERSDPATH + date + ".txt";
        final File logFile = FileUtils.getFileByPath(logFilePath);
        if (!FileUtils.createOrExistsFile(logFile)) {
            Log.e(TAG, "logWrite2File create " + logFilePath + "failure");
            return;
        }
        String time = df.format(now);
        final String logContent = time + ":[" + type + "]:" + tag + ":" + content + '\n';
        new Thread(new Runnable() {
            @Override
            public void run() {
                FileUtils.write2File(logFile, logContent, true);
            }
        }).start();
    }

}
