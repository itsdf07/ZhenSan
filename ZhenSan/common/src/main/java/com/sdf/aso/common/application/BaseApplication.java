package com.sdf.aso.common.application;

import android.app.Application;
import android.util.Log;

import com.sdf.aso.common.crash.CrashHandler;
import com.sdf.aso.common.db.manager.DbManager;
import com.sdf.aso.common.db.dao.DaoMaster;
import com.sdf.aso.common.db.dao.DaoSession;
import com.sdf.aso.common.file.FileUtils;
import com.sdf.aso.common.global.GlobalUtiles;

/**
 * 自定义Application
 * 1、初始化数据库相关
 * <p>
 * Created by itsdf07 on 2017/2/23 15:52.
 * E-Mail: 923255742@qq.com
 * GitHub: https://github.com/itsdf07
 */

public class BaseApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        FileUtils.INNERSDPATH = FileUtils.getInnerSDPath(getApplicationContext()) + GlobalUtiles.BASE_PATH;;
        Log.d("dfsu","INNERSDPATH = " + FileUtils.INNERSDPATH);
        DbManager.getInstance(getApplicationContext());//初始化数据库
        CrashHandler.getInstance().init(getApplicationContext());//注册CrashException，捕获崩溃
    }
}
