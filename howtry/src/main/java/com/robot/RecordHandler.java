package com.robot;

import android.os.Handler;
import android.os.Message;
import android.util.Log;

import java.lang.ref.WeakReference;

/**
 * Created by me_touch on 17-11-20.
 *
 */

public class RecordHandler extends Handler{
    public static final int MSG_VOLUME = 1;

    private WeakReference<DialogManager> manager;

    public RecordHandler(DialogManager manager){
        this.manager = new WeakReference<DialogManager>(manager);
    }

    @Override
    public void handleMessage(Message msg) {
        switch (msg.what){
            case MSG_VOLUME:
                if(manager.get() != null){
                    Log.e("RecordHandler","msg.arg1 = " + msg.arg1);
                    manager.get().updateVoiceLevel(msg.arg1);
                }
                break;
            default:
                break;
        }
    }
}
