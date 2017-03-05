package com.sdf.aso.common.db.utils;

import android.content.Context;

import com.sdf.aso.common.db.manager.HerosManager;

/**
 * 封装多表Manager对象
 * Created by itsdf07 on 2017/3/5 20:29.
 * E-Mail: 923255742@qq.com
 * GitHub: https://github.com/itsdf07
 */

public class DaoUtils {
    private static HerosManager mHerosManager;
    public static Context mContext;

    public static void init(Context context) {
        mContext = context.getApplicationContext();
    }

    /**
     * 单列模式获取CustomerManager对象
     *
     * @return
     */
    public static HerosManager getHerosInstance() {
        if (mHerosManager == null) {
            mHerosManager = new HerosManager();
        }
        return mHerosManager;
    }

}
