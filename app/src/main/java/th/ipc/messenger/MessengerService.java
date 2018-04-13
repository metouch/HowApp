package th.ipc.messenger;

import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.support.annotation.Nullable;

import th.how.bean.StudentEntity;

/**
 * Created by me_touch on 17-9-28.
 *
 */

public class MessengerService extends Service{

    private final static String TAG = "MessengerService";

    @Override
    public void onCreate() {
        super.onCreate();

    }

    private Messenger messenger = new Messenger(new Handler(){

        @Override
        public void handleMessage(Message msg) {
            if(msg.what == 1){
                System.out.println("Client Message comes");
                StudentEntity entity = (StudentEntity) msg.getData().getSerializable("student");
                System.out.println(entity);
                Message message = new Message();
                message.what = 2;
                entity.setNumber(200);
                entity.setName("哈哈");
                Bundle bundle = new Bundle();
                bundle.putSerializable("stu", entity);
                message.setData(bundle);
                try{
                    msg.replyTo.send(message);
                }catch(Exception e){
                    e.printStackTrace();
                }
            }
        }
    });

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return messenger.getBinder();
    }
}
