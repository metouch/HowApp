package th.ipc.aidl;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.os.RemoteCallbackList;
import android.os.RemoteException;
import android.support.annotation.Nullable;

import th.how.bean.IBinderPool;
import th.how.bean.IClientInterface;
import th.how.bean.IGetNewsEntity;
import th.how.bean.IMyAidlInterface;
import th.how.bean.ISetNewsEntity;
import th.how.bean.NewsEntity;
import th.how.utils.DpUtils;

/**
 * Created by me_touch on 2017/8/16.
 *
 */

public class RemoteService extends Service{

    private RemoteCallbackList<IClientInterface> mCallback = new RemoteCallbackList<>();

    private IBinder.DeathRecipient deathRecipient = new IBinder.DeathRecipient() {
        @Override
        public void binderDied() {

        }
    };

    @Override
    public void onCreate() {
        super.onCreate();
    }

    private final IGetNewsEntity.Stub getBinder = new IGetNewsEntity.Stub() {
        @Override
        public void getNewsEntity(NewsEntity entity) throws RemoteException {
            entity.setId(1);
            entity.setTitle("新闻1");
        }
    };

    private final ISetNewsEntity.Stub setBinder = new ISetNewsEntity.Stub() {
        @Override
        public void setNewsEntity(NewsEntity entity) throws RemoteException {
            System.out.println(entity.toString());
        }
    };

    private final IBinderPool.Stub mBinder = new IBinderPool.Stub() {
        @Override
        public IBinder getBinder(int code) throws RemoteException {
            callbackClient();
            return code == 1 ? setBinder : getBinder;
        }

        @Override
        public void register(IClientInterface callback) throws RemoteException {
            //可以多次设置,但不能超过上限
            callback.asBinder().linkToDeath(new DeathRecipientHandler(callback.asBinder()), 0);
            mCallback.register(callback);
        }

        @Override
        public void unRegister(IClientInterface callback) throws RemoteException {
            mCallback.unregister(callback);
        }
    };

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    @Override
    public boolean onUnbind(Intent intent) {
        return super.onUnbind(intent);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    private void callbackClient(){
        int num = mCallback.beginBroadcast();
        for(int i = 0; i < num; i ++){
            IClientInterface iClientInterface = mCallback.getBroadcastItem(i);
            if(iClientInterface != null){
                try{
                    iClientInterface.callbackClient();
                }catch(Exception e){
                    e.printStackTrace();
                }
            }
        }
        mCallback.finishBroadcast();
    }

    private class DeathRecipientHandler implements IBinder.DeathRecipient{

        private IBinder callBack;


        public DeathRecipientHandler(IBinder callBack){
            this.callBack = callBack;
        }
        @Override
        public void binderDied() {
            //清空客户端调用服务端的资源
            try{
                callBack.unlinkToDeath(this, 0);
            }catch(Exception e){
                e.printStackTrace();
            }
        }
    }
}
