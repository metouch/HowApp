package com.robot;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import com.example.howtry.R;

/**
 * Created by me_touch on 17-11-21.
 *
 */

public class DialogChooseContent {

    private Dialog mDialog;
    private Context context;

    private TextView tvContent;
    private OnChangeContentListener listener;

    private MessageBean bean;
    private String content;


    public DialogChooseContent(Context context){
        this.context = context;
    }

    public void setContent(String content) {
        this.content = content;
        if(tvContent != null)
            tvContent.setText(content);
    }

    public String getContent() {
        return content;
    }

    public void setListener(OnChangeContentListener listener) {
        this.listener = listener;
    }

    public void showDialog(final MessageBean bean){
        this.bean = bean;
        if(mDialog == null){
            mDialog = new Dialog(context);
            LayoutInflater inflater = LayoutInflater.from(context);
            View view = inflater.inflate(R.layout.dialog_choose_content, null);
            mDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            tvContent = (TextView)view.findViewById(R.id.dg_tv_content);
            tvContent.setText(content);
            if(listener != null){
                tvContent.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        listener.OnChangeContent(bean);
                        disMissDialog();
                    }
                });
            }
            mDialog.setContentView(view);
        }
        mDialog.show();
    }

    public void disMissDialog(){
        if(mDialog != null) {
            tvContent = null;
            content = null;
            listener = null;
            mDialog.dismiss();
            mDialog = null;
        }
    }

    public interface OnChangeContentListener{
        void OnChangeContent(MessageBean bean);
    }
}
