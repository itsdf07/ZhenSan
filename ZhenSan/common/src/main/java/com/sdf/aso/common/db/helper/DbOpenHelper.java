package com.sdf.aso.common.db.helper;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.sdf.aso.common.db.dao.DaoMaster;

/**
 * Created by itsdf07 on 2017/2/20 13:15.
 * E-Mail: 923255742@qq.com
 * GitHub: https://github.com/itsdf07
 */

public class DbOpenHelper extends DaoMaster.DevOpenHelper {
    /**
     * 用来存放在调用onCreate,onUpgrade,onDowngrade方法时出现的异常。
     * 针对以下崩溃做的特殊处理：
     * android.database.sqlite.SQLiteException: cannot rollback - no transaction is active
     * <p/>
     * 因为cannot rollback崩溃会吞掉(原因是finally块中的异常会覆盖掉try中的异常)onCreate,onUpgrade,onDowngrade抛出的异常。
     * 这种处理方式是为了 不让其吞掉异常。
     */
    Throwable mThrowable = null;

    public DbOpenHelper(Context context, String name, SQLiteDatabase.CursorFactory factory) {
        super(context, name, factory);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        try {
            super.onCreate(db);
        } catch (RuntimeException e) {
            mThrowable = e;
            throw e;
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion,
                            int newVersion) {
        try {
            Log.i("greenDAO", "Downing schema from version " + oldVersion
                    + " to " + newVersion + " by  by dropping all tables");
            DaoMaster.dropAllTables(db, true);
            DaoMaster.createAllTables(db, true);
        } catch (RuntimeException e) {
            mThrowable = e;
            throw e;
        }
    }

    @Override
    public SQLiteDatabase getReadableDatabase() {
        return super.getReadableDatabase();
    }

    @Override
    public SQLiteDatabase getWritableDatabase() {
        return super.getWritableDatabase();
    }

    public Throwable getThrowable() {
        return mThrowable;
    }
}
