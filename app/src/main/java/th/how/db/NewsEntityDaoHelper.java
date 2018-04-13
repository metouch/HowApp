package th.how.db;

import org.greenrobot.greendao.async.AsyncOperationListener;
import org.greenrobot.greendao.async.AsyncSession;
import org.greenrobot.greendao.query.LazyList;
import org.greenrobot.greendao.query.QueryBuilder;

import java.util.List;

import th.how.app.AppApplication;
import th.how.bean.NewsEntity;

/**
 * Created by me_touch on 2017/8/18.
 *
 */

public class NewsEntityDaoHelper {

    private volatile static NewsEntityDaoHelper helper;
    private NewsEntityDao dao;

    private NewsEntityDaoHelper(){
        this.dao = AppApplication.getInstance().getHelper().getDaoSession().getNewsEntityDao();
    }

    public static NewsEntityDaoHelper getInstance(){
        if(helper == null){
            synchronized (NewsEntityDaoHelper.class){
                if(helper == null){
                    helper = new NewsEntityDaoHelper();
                }
            }
        }
        return helper;
    }

    public void insert(NewsEntity data){
        dao.insertOrReplace(data);
    }

    public void insertList(List<NewsEntity> list){
        dao.insertOrReplaceInTx(list);
    }

    public void asyncOperation(Runnable runnable, AsyncOperationListener listener){
        AsyncSession session = dao.getSession().startAsyncSession();
        session.setListenerMainThread(listener);
        session.runInTx(runnable);
    }
}
