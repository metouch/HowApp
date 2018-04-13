package th.ipc.aidl;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import th.how.app.R;
import th.how.bean.IBinderPool;
import th.how.bean.IClientInterface;
import th.how.bean.IGetNewsEntity;
import th.how.bean.IMyAidlInterface;
import th.how.bean.ISetNewsEntity;
import th.how.bean.NewsEntity;

public class AidlClientActivity extends AppCompatActivity {

    private IBinderPool iService;
    private IGetNewsEntity getService;
    private ISetNewsEntity setService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aidl_client);
        Intent intent = new Intent(this, RemoteService.class);
        intent.setAction(RemoteService.class.getName());
        bindService(intent, mConnection, BIND_AUTO_CREATE);
    }

    private IBinder.DeathRecipient deathRecipient = new IBinder.DeathRecipient() {
        @Override
        public void binderDied() {
            try {
                iService.asBinder().unlinkToDeath(deathRecipient, 0);
            }catch (Exception e){
                e.printStackTrace();
            }finally {
                iService = null;
            }

        }
    };

    private IClientInterface client = new IClientInterface.Stub() {
        @Override
        public void callbackClient() throws RemoteException {
            System.out.println("Client: Thread.currentThread().getName() = " + Thread.currentThread().getName());
        }
    };

    private ServiceConnection mConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            try {
                iService = IBinderPool.Stub.asInterface(service);
                iService.asBinder().linkToDeath(deathRecipient, 0);
                iService.register(client);
                setService = ISetNewsEntity.Stub.asInterface(iService.getBinder(1));
                getService = IGetNewsEntity.Stub.asInterface(iService.getBinder(2));
                NewsEntity entity = new NewsEntity();
                getService.getNewsEntity(entity);
                System.out.println(" entity = " + entity);
                setService.setNewsEntity(entity);
            }catch (Exception e){
                e.printStackTrace();
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbindService(mConnection);
    }
}
