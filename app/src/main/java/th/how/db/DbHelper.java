package th.how.db;

import android.content.ContentResolver;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import org.greenrobot.greendao.query.QueryBuilder;

import th.how.app.AppHelper;
import th.how.app.BuildConfig;
import th.how.bean.NewsEntity;

/**
 * Created by me_touch on 17-10-23.
 *
 */

public class DbHelper {

    public static volatile DbHelper helper;

    private DaoMaster.DevOpenHelper SQLiteOpenHelper;
    private DaoSession daoSession;
    private SQLiteDatabase writableDb;
    private SQLiteDatabase readableDb;

    static {
        QueryBuilder.LOG_VALUES = QueryBuilder.LOG_SQL = BuildConfig.DEBUG;
    }

    public static DbHelper getInstance(Context context){
        if(helper == null){
            synchronized (DbHelper.class){
                helper = helper == null
                        ? new DbHelper(context.getApplicationContext(), AppHelper.DB_NAME)
                        : helper;
            }
        }
        return helper;
    }

    private DbHelper(Context context, String dbName){
        SQLiteOpenHelper = new DaoMaster.DevOpenHelper(context, dbName);
    }

    public DaoMaster.DevOpenHelper getSQLiteOpenHelper() {
        return SQLiteOpenHelper;
    }

    public DaoSession getDaoSession() {
        if(daoSession != null)
            return daoSession;
        synchronized (this){
            if(daoSession == null){
                daoSession = new DaoMaster(SQLiteOpenHelper.getWritableDb()).newSession();
            }
        }
        return daoSession;
    }

    public void insert(){
        long time = System.currentTimeMillis();
        NewsEntityDao dao = getDaoSession().getNewsEntityDao();
        dao.insertOrReplace(new NewsEntity(1, "新闻1"));
        System.out.println("InsertTime = " + (System.currentTimeMillis() - time));
    }

    public void query(){
        long time = System.currentTimeMillis();
        NewsEntityDao dao = getDaoSession().getNewsEntityDao();
        QueryBuilder<NewsEntity> qb = dao.queryBuilder();
        qb.where(NewsEntityDao.Properties.Id.eq(1)).build();
        System.out.println(qb.list());
        System.out.println("QueryTime = " + (System.currentTimeMillis() - time));
    }

    private synchronized void newDevSession(){

    }
}
