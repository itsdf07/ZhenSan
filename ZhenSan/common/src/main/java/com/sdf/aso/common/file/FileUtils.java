package com.sdf.aso.common.file;

import android.content.Context;
import android.os.Environment;
import android.os.storage.StorageManager;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;

import com.sdf.aso.common.StringUtils;
import com.sdf.aso.common.global.GlobalUtiles;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * 1、根据文件路径获取文件：getFileByPath(String filePath)
 * 2、判断目录是否存在：createOrExistsDir(File file)
 * 判断目录是否存在：如果存在，是目录则返回true，是文件则返回false，不存在则返回是否创建成功
 * 3、判断文件是否存在：createOrExistsFile(File file)
 * 判断文件是否存在，不存在则判断是否创建成功
 * 4、判断文件是否存在：isFileExists(String filePath)
 * 5、文件写入
 * Created by itsdf07 on 2017/3/6 14:56.
 * E-Mail: 923255742@qq.com
 * GitHub: https://github.com/itsdf07
 */

public class FileUtils {
    private static final String TAG = FileUtils.class.getSimpleName();
    public static String INNERSDPATH = Environment.getExternalStorageDirectory().getAbsolutePath() + GlobalUtiles.BASE_PATH;

    private FileUtils() {
        throw new UnsupportedOperationException("Sorry,Instantiation [FileUtils.java] Failure!");
    }

    /**
     * 根据文件路径获取文件
     *
     * @param filePath 文件路径
     * @return 文件
     */
    public static File getFileByPath(String filePath) {
        return StringUtils.isAvailable(filePath) ? null : new File(filePath);
    }

    /**
     * 判断文件是否存在
     *
     * @param filePath 文件路径
     * @return {@code true}: 存在<br>{@code false}: 不存在
     */
    public static boolean isFileExists(String filePath) {
        File file = getFileByPath(filePath);
        return isFileExists(file);
    }

    /**
     * 判断文件是否存在
     *
     * @param file 文件
     * @return {@code true}: 存在<br>{@code false}: 不存在
     */
    public static boolean isFileExists(File file) {
        return file != null && file.exists();
    }

    /**
     * 判断目录是否存在：如果存在，是目录则返回true，是文件则返回false，不存在则返回是否创建成功
     *
     * @param file 文件
     * @return {@code true}: 存在或创建成功<br>{@code false}: 不存在或创建失败
     */
    public static boolean createOrExistsDir(File file) {
        // 如果存在，是目录则返回true，是文件则返回false，不存在则返回是否创建成功
        return file != null && (file.exists() ? file.isDirectory() : file.mkdirs());
    }

    /**
     * 判断文件是否存在，不存在则判断是否创建成功
     *
     * @param filePath 文件路径
     * @return {@code true}: 存在或创建成功<br>{@code false}: 不存在或创建失败
     */
    public static boolean createOrExistsFile(String filePath) {
        File file = getFileByPath(filePath);
        return createOrExistsFile(file);
    }

    /**
     * 判断文件是否存在，不存在则判断是否创建成功
     *
     * @param file 文件
     * @return {@code true}: 存在或创建成功<br>{@code false}: 不存在或创建失败
     */
    public static boolean createOrExistsFile(File file) {
        if (file == null) {
            return false;
        }
        // 如果存在，是文件则返回true，是目录则返回false
        if (file.exists()) {
            return file.isFile();
        }
        if (!createOrExistsDir(file.getParentFile())) {//创建文件目录
            return false;
        }
        try {
            return file.createNewFile();//创建文件
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }


    /**
     * 信息写入文件
     *
     * @param file    写入的文件
     * @param content 写入的数据
     * @param append  是否覆盖写入
     * @return {@code true}: 信息写入成功<br>{@code false}: 信息写入失败
     */
    public static boolean write2File(File file, String content, boolean append) {
        content = StringUtils.null2Length0(content);
        byte[] aData = content.getBytes();
        return write2File(file, aData, append);
    }

    /**
     * 信息写入文件
     *
     * @param file   写入的文件
     * @param aData  写入的数据
     * @param append 是否覆盖写入
     * @return {@code true}: 信息写入成功<br>{@code false}: 信息写入失败
     */
    public static boolean write2File(File file, byte[] aData, boolean append) {
        OutputStream out = null;
        boolean ok = false;
        try {
            out = new FileOutputStream(file, append);
            out.write(aData);
            ok = true;
        } catch (Exception e) {
            Log.e(TAG, "File is written to failure ：", e);
        } finally {
            if (out != null) {
                try {
                    out.close();
                } catch (IOException e) {
                }
            }
        }
        return ok;
    }

    /**
     * 获取内置sd卡路径，<br/>
     * 目前个人认为可以移除就是外卡，不能移除就是内卡，只经过大量试验，没有任何依据！此处需慎重！！！
     *
     * @param context
     * @return 内卡路径
     */
    public static String getInnerSDPath(Context context) {

        int currentapiVersion = android.os.Build.VERSION.SDK_INT;
        if (currentapiVersion >= 12) {
            try {
                StorageManager manager = (StorageManager) context.getSystemService(Context.STORAGE_SERVICE);
                /************** StorageManager的方法 ***********************/
                Method getVolumeList = StorageManager.class.getMethod("getVolumeList");
                Method getVolumeState = StorageManager.class.getMethod("getVolumeState", String.class);

                Object[] Volumes = (Object[]) getVolumeList.invoke(manager);
                String state = null;
                String path = null;

                for (Object volume : Volumes) {
                    /************** StorageVolume的方法 ***********************/
                    Method getPath = volume.getClass().getMethod("getPath");
                    path = (String) getPath.invoke(volume);
                    state = (String) getVolumeState.invoke(manager, getPath.invoke(volume));

                    /**
                     * 是否可以移除(内置sdcard) TODO:
                     * 目前个人认为可以移除就是外卡，不能移除就是内卡，只经过大量试验，没有任何依据！此处需慎重！！！
                     */
                    Method isRemovable = volume.getClass().getMethod("isRemovable");
                    boolean removable = (Boolean) isRemovable.invoke(volume);

                    if (null != path && null != state && state.equals(Environment.MEDIA_MOUNTED)) {
                        if (false == removable) {
                            return path;
                        }
                    }
                }
            } catch (Exception e) {
                return "";
            }
        }
        // 得到存储卡路径
        File sdDir = null;
        boolean sdCardExist = Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED); // 判断sd卡
        // 或可存储空间是否存在
        if (sdCardExist) {
            sdDir = Environment.getExternalStorageDirectory();// 获取sd卡或可存储空间的跟目录
            return sdDir.toString();
        }

        return "";
    }

    public static String getAssetsResource(Context context, String file) {
        InputStream inputStream = null;
        try {
            inputStream = context.getResources().getAssets().open(file);//读取assets/下的文件
        } catch (IOException e) {
            e.printStackTrace();
        }
        return getString(inputStream);
    }

    public static String getRawResource(Context context, int raw) {
        InputStream inputStream = context.getResources().openRawResource(raw);//读取res/raw/下的文件
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
