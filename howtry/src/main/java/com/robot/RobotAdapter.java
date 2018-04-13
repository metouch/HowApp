package com.robot;

import android.content.Context;
import android.graphics.drawable.ShapeDrawable;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.howtry.R;

import java.util.List;



/**
 * Created by zmm on 2017-11-06.
 *
 */

public class RobotAdapter extends BaseAdapter {

    private List<MessageBean> list;
    private Context context;
    private OnPlayListener listener;

    private static final int TYPE_COUNT = 3;

    //
    public RobotAdapter(Context context, List<MessageBean> list) {
        this.list = list;
        this.context = context;
    }

    public void setListener(OnPlayListener listener) {
        this.listener = listener;
    }

    @Override
    public int getCount() {
        return list == null ? 0 : list.size();
    }

    @Override
    public MessageBean getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }


    @Override
    public int getItemViewType(int position) {
        MessageBean bean = list.get(position);
        if (Constans.MessageStatus.FROM == bean.getStatus()) {
            return Constans.MessageStatus.FROM;
        } else {
            return Constans.MessageStatus.TO;
        }
    }

    @Override
    public int getViewTypeCount() {
        return TYPE_COUNT;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        MyViewHolder myViewHolder = null;
        MessageBean messageBean = list.get(position);
        int itemViewType = getItemViewType(position);
        switch (itemViewType) {
            case Constans.MessageStatus.FROM:
                if (convertView == null) {
                    convertView = LayoutInflater.from(context).inflate(R.layout.item_robot_send, parent, false);
                    myViewHolder = new MyViewHolder(convertView);
                    convertView.setTag(myViewHolder);
                } else {
                    myViewHolder = (MyViewHolder) convertView.getTag();
                }
                break;
            case Constans.MessageStatus.TO:
                if (convertView == null) {
                    convertView = LayoutInflater.from(context).inflate(R.layout.item_robot, parent, false);
                    myViewHolder = new MyViewHolder(convertView);
                    convertView.setTag(myViewHolder);
                } else {
                    myViewHolder = (MyViewHolder) convertView.getTag();
                }
                break;
        }
        myViewHolder.bean = messageBean;
        long time = messageBean.getTime();
        String content = messageBean.getContent();
        String photo = messageBean.getPhoto();
        if(myViewHolder.bean.getType() == 1){
            LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) myViewHolder.fmContent.getLayoutParams();
            lp.width = LinearLayout.LayoutParams.WRAP_CONTENT;
            myViewHolder.fmContent.setLayoutParams(lp);
            myViewHolder.tvContent.setVisibility(View.VISIBLE);
            myViewHolder.ivVoice.setVisibility(View.GONE);
        }else {
            LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) myViewHolder.fmContent.getLayoutParams();
            lp.width = Utils.dip2px(context, 100);
            myViewHolder.fmContent.setLayoutParams(lp);
            myViewHolder.tvContent.setVisibility(View.GONE);
            myViewHolder.ivVoice.setVisibility(View.VISIBLE);
        }
        myViewHolder.setListener(listener);
        if (time != 0) {
            myViewHolder.tvTime.setText(Utils.getDateToString(time));
        }
        if (!TextUtils.isEmpty(content)) {
            int index = content.length();
            try{
                if(content.contains("http://localNews") && (messageBean.getNewsId() == -1
                        || messageBean.getNewsType() == -1)){
                    index = content.indexOf("http://localNews");
                    String news = content.substring(index);
                    news = news.toLowerCase();
                    String[] params = news.split("&");
                    for(int i = 0; i < params.length; i++){
                        if(params[i].contains("id=")){
                            int idIndex = params[i].indexOf("=");
                            messageBean.setNewsId(Integer.valueOf(params[i].substring(idIndex + 1)));
                            continue;
                        }
                        if(params[i].contains("newstype=")){
                            int idIndex = params[i].indexOf("=");
                            messageBean.setNewsType(Integer.valueOf(params[i].substring(idIndex + 1)));
                        }
                    }
                }
            }catch(Exception e){
                e.printStackTrace();
                messageBean.setNewsId(-1);
                messageBean.setNewsType(-1);
            }
            if(messageBean.getNewsId() >= 0 && messageBean.getNewsType() >=0){
                myViewHolder.tvContent.setTextColor(0xff3eb4e6);
            }else {
                myViewHolder.tvContent.setTextColor(0xff000000);
            }
            myViewHolder.tvContent.setText(content.substring(0, index));
        }
        int status = messageBean.getStatus();
        if (!TextUtils.isEmpty(photo)) {
            ImageLoadUtils.loadCircle(context, photo, myViewHolder.ivPhoto);
        }
        return convertView;
    }

    public static class MyViewHolder {

        FrameLayout fmContent;
        ImageView ivPhoto;
        ImageView ivVoice;
        TextView tvContent;
        TextView tvTime;
        MessageBean bean;

        public MyViewHolder(View itemView) {
            fmContent = (FrameLayout)itemView.findViewById(R.id.bubble);
            ivPhoto = (ImageView) itemView.findViewById(R.id.iv_photo);
            ivVoice = (ImageView)itemView.findViewById(R.id.iv_content);
            tvContent = (TextView) itemView.findViewById(R.id.tv_content);
            tvTime = (TextView) itemView.findViewById(R.id.tv_time);
        }

        public void setListener(final OnPlayListener listener){
            if(ivVoice == null) return;
            if(ivVoice.getVisibility() == View.VISIBLE){
                ivVoice.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        listener.onPlay(ivVoice, bean);
                    }
                });
            }else {
                ivVoice.setOnClickListener(null);
            }
        }
    }

    public interface OnPlayListener{
        void onPlay(ImageView ivVoice, MessageBean bean);
    }
}
