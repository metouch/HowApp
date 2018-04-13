package th.how.app;

import android.app.Application;
import android.content.Intent;
import android.net.Uri;

import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.query.QueryBuilder;

import th.how.db.DaoMaster;
import th.how.db.DaoSession;
import th.how.db.DbHelper;

/**
 * Created by me_touch on 2017/8/18.
 *
 */

public class AppApplication extends Application{

    private static AppApplication application;
    private DaoSession mDaoSession;
    private DbHelper helper;

    public static AppApplication getInstance(){
        return application;
    }
    @Override
    public void onCreate() {
        Uri uri = android.net.Uri.parse("content://th.how.app.provider" + "/NEWS_ENTITY");
        QueryBuilder.LOG_VALUES = QueryBuilder.LOG_SQL = BuildConfig.DEBUG;
        long preTime = System.currentTimeMillis();
        super.onCreate();
        System.out.println("delay = " + (System.currentTimeMillis() - preTime));
        application = this;
        helper = DbHelper.getInstance(this);
        grantUriPermission("th.how.test", uri, Intent.FLAG_GRANT_READ_URI_PERMISSION);
        //getOrCreateDb();
        createDb();
    }

    private void createDb(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                DaoSession session = helper.getDaoSession();
                if(session != mDaoSession){
                    System.out.println("XXXXXXXXXXXXXXXXXXXXXXXXXXXXX");
                    System.out.println("LLLLLLLLLLLLLLLLLLLLLLLLLLLL");
                    mDaoSession = session;
                }
            }
        }).start();
    }

    public DbHelper getHelper() {
        return helper;
    }

    public DaoSession getDaoSession() {
        return mDaoSession;
    }

    //it's terrible
    private void getOrCreateDb(){
        QueryBuilder.LOG_VALUES = QueryBuilder.LOG_SQL = BuildConfig.DEBUG;
//        mDaoSession = DaoMaster.newDevSession(this, AppHelper.DB_NAME);
        long preTime = System.currentTimeMillis();
        DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(this, AppHelper.DB_NAME);
        System.out.println("delay = " + (System.currentTimeMillis() - preTime));
        Database db = helper.getWritableDb();
        System.out.println("currentDelay = " + (System.currentTimeMillis() - preTime));
        DaoMaster daoMaster = new DaoMaster(db);
        mDaoSession = daoMaster.newSession();
    }


}
