package com.sdf.aso.common.db.manager;

import android.content.ContentValues;
import android.content.Context;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;

import android.database.sqlite.SQLiteDatabase.CursorFactory;

import com.sdf.aso.common.db.dao.DaoMaster;
import com.sdf.aso.common.db.dao.DaoSession;
import com.sdf.aso.common.db.helper.DbOpenHelper;

import java.io.File;
import java.util.Locale;

import de.greenrobot.dao.identityscope.IdentityScopeType;
import de.greenrobot.dao.query.QueryBuilder;

/**
 * 数据库管理
 * 1、更新loccale字段，防止切换语言后系统重建索引操作
 * 2、创建数据库
 * 3、实例出DaoMaster、DaoSession、helper
 * <p>
 * <p>
 * Created by itsdf07 on 2017/2/19 17:22.
 * E-Mail: 923255742@qq.com
 * GitHub: https://github.com/itsdf07
 */

public class DbManager {
    public static final String DB_NAME = "zhensan.db";
    private static Context mContext;
    private static DbManager mInstance;
    private static DaoMaster mDaoMaster;
    private static DaoSession mDaoSession;
    private static DbOpenHelper mHelper;


    /**
     * 1、检查数据库相关：
     * 2、取得DaoMaster和DaoSession
     */
    private DbManager() {

        updateLocale(mContext, DB_NAME);

        getDaoMaster();
        getDaoSession();
    }

    /**
     * 初始化Context对象
     *
     * @param context
     */
    public void init(Context context) {
        mContext = context;
    }

    /**
     * 备注(切记)：使用前需先初始化init(Context context)；
     * 单例模式获取操作数据库的对象
     *
     * @return
     */
    public static DbManager getInstance() {
        if (mInstance == null) {
            synchronized (DbManager.class) {
                mInstance = new DbManager();
            }
        }
        return mInstance;
    }

    /**
     * 设置debug模式开启或关闭，默认关闭
     *
     * @param flag
     */
    public void setDebug(boolean flag) {
        QueryBuilder.LOG_SQL = flag;
        QueryBuilder.LOG_VALUES = flag;
    }

    /**
     * 取得DaoMaster
     *
     * @return daoMaster
     */
    private DaoMaster getDaoMaster() {
        if (null == mDaoMaster) {
            try {
                mHelper = new DbOpenHelper(mContext, DB_NAME, null);
                mDaoMaster = new DaoMaster(mHelper.getWritableDatabase());
            } catch (RuntimeException e) {
                if (mHelper.getThrowable() != null && e != mHelper.getThrowable()) {
                    try {
                        e.initCause(mHelper.getThrowable());
                    } catch (Throwable ex) {
                        ex.printStackTrace();
                    }
                }
                throw e;
            }
        }

        return mDaoMaster;
    }

    /**
     * 取得DaoSession
     *
     * @return daoSession
     */
    public DaoSession getDaoSession() {
        if (mDaoSession == null) {
            if (mDaoMaster == null) {
                mDaoMaster = getDaoMaster();
            }
            mDaoSession = mDaoMaster.newSession(IdentityScopeType.None);
        }
        return mDaoSession;
    }


    /**
     * 更新loccale字段，防止切换语言后系统重建索引操作。
     * 尝试解决如下崩溃：android.database.sqlite.SQLiteException: Failed to change locale for db '/data/data/com.autonavi.minimap/databases/aMap.db' to 'zh_CN'.
     *
     * @param context
     * @param dbString
     */
    private void updateLocale(Context context, String dbString) {
        try {
            String path = context.getDatabasePath(dbString).getPath();
            if (!(new File(path)).exists()) {
                return;
            }

            SQLiteDatabase db = SQLiteDatabase.openDatabase(path, (CursorFactory) null, 16, (DatabaseErrorHandler) null);
            if (db == null) {
                return;
            }

            try {
                String e = Locale.getDefault().toString();//获取当前系统默认的语言
                ContentValues contentValues = new ContentValues();
                contentValues.put("locale", e);
                db.update("android_metadata", contentValues, "locale!=\'" + e + "\'", (String[]) null);
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                db.close();
            }
        } catch (Throwable t) {
        }

    }

    /**
     * 关闭数据库
     */
    public void closeDataBase() {
        closeHelper();
        closeDaoSession();
    }

    public void closeDaoSession() {
        if (null != mDaoSession) {
            mDaoSession.clear();
            mDaoSession = null;
        }
    }

    public void closeHelper() {
        if (mHelper != null) {
            mHelper.close();
            mHelper = null;
        }
    }

//    原理简介
//    GreenDao向SQLite数据库提供了一个对象导向的接口，它为用户省下了很多重复的工作，而且提供了简便的操作接口。
//    为了使用GreenDao，需要在新建一个Java工程（工程中需要导入greendao-generator-x.x.x.jar，freemarker-x.x.xx.jar），根据GreenDao的规则在其中描述数据库的表结构，运行之后它会构建你的实体模型和DAO工具类。具体包括：
//
//    DaoMaster:
//    持有数据库对象(SQLiteDatabase) ，并管理一些DAO类(不是对象)
//    能够创建和删除数据库表
//    它的内部类OpenHelper和DevOpenHelper是SQLiteOpenHelper的实现类，用于创建SQLite数据库的模式

//    DaoSession:
//    管理制定模式下所有可用的DAO对象
//    能对实体进行插入、加载、更新、刷新、删除操作。

//    DAO:
//    每个实体都有一个DAO，相对于DaoSession,它有更多的方法，比如：加载全部、InsertTx

//    Entity
//    可持久化的对象，由generator 生成。相当于数据库中的一张表，所有字段都是使用标准的Java对象的属性(比诶)
//    通过generator生成的这些工具类，你就可以在自己的Android工程中对进行数据库操作，完全不需要写任何SQL语句。


}
