package com.sdf.aso.common.db.dao;

import android.content.Context;
import android.util.Log;

import com.sdf.aso.common.db.manager.DbManager;

import java.util.List;

/**
 * Created by itsdf07 on 2017/3/5 16:57.
 * E-Mail: 923255742@qq.com
 * GitHub: https://github.com/itsdf07
 */

public class BaseDao<T> {
    private static final String TAG = BaseDao.class.getSimpleName();
    public static final boolean DUBUG = true;//debug
    public DbManager mDbManager;
    public DaoSession mDaoSession;

    public BaseDao(Context context) {
        mDbManager = DbManager.getInstance(context);
        mDbManager.init(context);
        mDaoSession = mDbManager.getDaoSession();
        mDbManager.setDebug(DUBUG);
    }

    /**************************数据库操作insert object***********************/

    /**
     * 插入单个对象
     *
     * @param object
     * @return true:insert is successful
     */
    public boolean insertObject(T object) {
        boolean flag = false;
        try {
            //the row ID of the last row inserted, if this insert is successful. -1 otherwise.
            flag = mDaoSession.insertOrReplace(object) != -1 ? true : false;
        } catch (Exception e) {
            Log.e(TAG, e.toString());
        }
        return flag;
    }

    /**
     * 开启新的线程插入多个对象
     *
     * @param objects
     * @return
     */
    public boolean insertObject(final List<T> objects) {
        boolean flag = false;
        if (null == objects || objects.isEmpty()) {
            return false;
        }
        try {
            mDaoSession.runInTx(new Runnable() {
                @Override
                public void run() {
                    for (T object : objects) {
                        insertObject(object);
                    }
                }
            });
            flag = true;
        } catch (Exception e) {
            Log.e(TAG, e.toString());
            flag = false;
        } finally {
//            manager.CloseDataBase();
        }
        return flag;
    }

    /**************************数据库操作del object***********************/
    /**
     * 删除某个数据库表
     *
     * @param clss
     * @return
     */
    public boolean deleteAll(Class clss) {
        boolean flag = false;
        try {
            mDaoSession.deleteAll(clss);
            flag = true;
        } catch (Exception e) {
            Log.e(TAG, e.toString());
            flag = false;
        }
        return flag;
    }

    /**
     * 删除某个对象
     *
     * @param object
     * @return
     */
    public void deleteObject(T object) {
        try {
            mDaoSession.delete(object);
        } catch (Exception e) {
            Log.e(TAG, e.toString());
        }
    }

    /**
     * 异步批量删除数据
     *
     * @param objects
     * @return
     */
    public boolean deleteObject(final List<T> objects, Class clss) {
        boolean flag = false;
        if (null == objects || objects.isEmpty()) {
            return false;
        }
        try {

            mDaoSession.getDao(clss).deleteInTx(new Runnable() {
                @Override
                public void run() {
                    for (T object : objects) {
                        deleteObject(object);
                    }
                }
            });
            flag = true;
        } catch (Exception e) {
            Log.e(TAG, e.toString());
            flag = false;
        }
        return flag;
    }

    /**************************数据库操作query object***********************/

    /**
     * 获得某个表名
     *
     * @return
     */
    public String getTableName(Class object) {
        return mDaoSession.getDao(object).getTablename();
    }

    /**
     * 查询某个ID的对象是否存在
     *
     * @param
     * @return
     */
//    public boolean isExitObject(long id, Class object) {
//        QueryBuilder<T> qb = (QueryBuilder<T>) mDaoSession.getDao(object).queryBuilder();
//        qb.where(CustomerDao.Properties.Id.eq(id));
//        long length = qb.buildCount().count();
//        return length > 0 ? true : false;
//    }

    /**
     * 根据主键ID来查询
     *
     * @param id
     * @return
     */
    public T queryById(long id, Class object) {
        return (T) mDaoSession.getDao(object).loadByRowId(id);
    }

    /**
     * 查询某条件下的对象
     *
     * @param object
     * @return
     */
    public List<T> queryObject(Class object, String where, String... params) {
        Object obj = null;
        List<T> objects = null;
        try {
            obj = mDaoSession.getDao(object);
            if (null == obj) {
                return null;
            }
            objects = mDaoSession.getDao(object).queryRaw(where, params);
        } catch (Exception e) {
            Log.e(TAG, e.toString());
        }

        return objects;
    }

    /**
     * 查询所有对象
     *
     * @param object
     * @return
     */
    public List<T> queryAll(Class object) {
        List<T> objects = null;
        try {
            objects = (List<T>) mDaoSession.getDao(object).loadAll();
        } catch (Exception e) {
            Log.e(TAG, e.toString());
        }
        return objects;
    }

    /**************************数据库操作update object***********************/
    /**
     * 以对象形式进行数据修改
     * 其中必须要知道对象的主键ID
     *
     * @param object
     * @return
     */
    public void updateObject(T object) {

        if (null == object) {
            return;
        }
        try {
            mDaoSession.update(object);
        } catch (Exception e) {
            Log.e(TAG, e.toString());
        }
    }

    /**
     * 批量更新数据
     *
     * @param objects
     * @return
     */
    public void updateObject(final List<T> objects, Class clss) {//
        if (null == objects || objects.isEmpty()) {
            return;
        }
        try {

            mDaoSession.getDao(clss).updateInTx(new Runnable() {
                @Override
                public void run() {
                    for (T object : objects) {
                        updateObject(object);
                    }
                }
            });
        } catch (Exception e) {
            Log.e(TAG, e.toString());
        }
    }

    /***************************关闭数据库*************************/
    /**
     * 关闭数据库一般在onDestory中使用
     */
    public void closeDataBase() {
        mDbManager.closeDataBase();
    }

}
