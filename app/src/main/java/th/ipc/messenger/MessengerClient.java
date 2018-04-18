package th.ipc.messenger;

import android.app.Activity;
import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.content.ServiceConnection;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import java.util.concurrent.atomic.AtomicInteger;

import th.how.app.AppApplication;
import th.how.app.R;
import th.how.bean.StudentEntity;
import th.how.db.DbHelper;
import th.how.db.NewsEntityDao;
import th.ipc.provider.presenter.NewsContentProvider;

/**
 * Created by me_touch on 17-9-29.
 *
 */

public class MessengerClient extends AppCompatActivity{

    private final static String TAG = "MessengerClient";

    private boolean bindOrNot;

    private Messenger mService;


    private Uri uri = Uri.parse(NewsContentProvider.BASE_CONTENT_URI + "/" + NewsEntityDao.TABLENAME);


    private ServiceConnection connection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            mService = new Messenger(service);
            Message message = new Message();
            message.replyTo = messenger;
            message.what = 1;
            Bundle bundle = new Bundle();
            StudentEntity entity = new StudentEntity(100, "呵呵");
            bundle.putSerializable("student", entity);
            message.setData(bundle);
            try{
                mService.send(message);
            }catch(Exception e){
                e.printStackTrace();
            }

        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    };

    private Messenger messenger = new Messenger(new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if(msg.what == 2) {
                StudentEntity entity = (StudentEntity) msg.getData().getSerializable("stu");
                System.out.println(entity.toString());
                System.out.println("Service Message comes");
            }
        }
    });

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        System.out.println("createTime = " + System.currentTimeMillis());
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aidl_client);
//        AppApplication.getInstance().getHelper().insert();
//        AppApplication.getInstance().getHelper().query();
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                startService();
            }
        }, 2000);

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                dbOperation();
            }
        }, 5000);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(bindOrNot){
            unbindService(connection);
            bindOrNot = false;
        }
    }

    private void startService(){
        Intent intent = new Intent(this, MessengerService.class);
        bindService(intent, connection, BIND_AUTO_CREATE);
        bindOrNot = true;
    }

    private void dbOperation(){
        ContentValues values = new ContentValues();
        values.put(NewsEntityDao.Properties.Id.columnName, 1);
        values.put(NewsEntityDao.Properties.Title.columnName, "标题1");
        values.put(NewsEntityDao.Properties.SubTitle.columnName, "Z副标题1");
        getContentResolver().insert(uri, values);
        values.clear();
        values.put(NewsEntityDao.Properties.Id.columnName, 2);
        values.put(NewsEntityDao.Properties.Title.columnName, "标题2");
        values.put(NewsEntityDao.Properties.SubTitle.columnName, "X副标题2");
        getContentResolver().insert(uri, values);
        values.clear();
        values.put(NewsEntityDao.Properties.Id.columnName, 3);
        values.put(NewsEntityDao.Properties.Title.columnName, "标题3");
        values.put(NewsEntityDao.Properties.SubTitle.columnName, "Y副标题3");
        getContentResolver().insert(uri, values);
        values.clear();
        values.put(NewsEntityDao.Properties.Id.columnName, 4);
        values.put(NewsEntityDao.Properties.Title.columnName, "标题4");
        values.put(NewsEntityDao.Properties.SubTitle.columnName, "V副标题4");
        getContentResolver().insert(uri, values);
        values.clear();
        values.put(NewsEntityDao.Properties.Id.columnName, 5);
        values.put(NewsEntityDao.Properties.Title.columnName, "标题5");
        values.put(NewsEntityDao.Properties.SubTitle.columnName, "W副标题5");
        getContentResolver().insert(uri, values);

        String[] projection = new String[]{NewsEntityDao.Properties.Id.columnName,
                NewsEntityDao.Properties.Title.columnName, NewsEntityDao.Properties.SubTitle.columnName};

        String selection = "TITLE LIKE ?";

        String[] selectionArgs = new String[]{"标题%"};

        String sortOrder = NewsEntityDao.Properties.SubTitle.columnName;
        Cursor cursor = getContentResolver().query(uri, projection, selection, selectionArgs, sortOrder);
        while (cursor.moveToNext()){
            Log.d(TAG, "id = " + cursor.getLong(0) + ", title = " + cursor.getString(1) + ", subtitle = " + cursor.getString(2));
        }
    }
}
