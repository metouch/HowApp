package th.how.page.main.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;


import java.util.List;

import th.how.app.R;
import th.how.bean.MessageBean;
import th.how.utils.ImageLoadUtils;
import th.how.utils.Utils;


/**
 * Created by zmm on 2017-11-06.
 */

public class RobotAdapter extends BaseAdapter {

    private List<MessageBean> list;
    private Context context;

    private static final int TYPE_COUNT = 3;

    //
    public RobotAdapter(Context context, List<MessageBean> list) {
        this.list = list;
        this.context = context;
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
        MyViewHolder myViewHolder1 = null;
        MessageBean messageBean = list.get(position);
        int status = messageBean.getStatus();
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
        long time = messageBean.getTime();
        String content = messageBean.getContent();
        String photo = messageBean.getPhoto();
        if (time != 0) {
            myViewHolder.tvTime.setText(Utils.getDateToString(time));
        }
        if (!TextUtils.isEmpty(content)) {
            myViewHolder.tvContent.setText(content);
        }
        if (!TextUtils.isEmpty(photo)) {
            ImageLoadUtils.loadCircle(context, photo, myViewHolder.ivPhoto);
        } else {
            if (Constans.MessageStatus.FROM == status) {//发送方
                ImageLoadUtils.loadCircle(context, R.mipmap.ic_launcher, myViewHolder.ivPhoto);
            } else {//接收方，也就是机器人
                ImageLoadUtils.loadCircle(context, R.mipmap.jqr, myViewHolder.ivPhoto);
            }
//            ImageLoadUtils.loadCircle(context, R.mipmap.jqr, myViewHolder.ivPhoto);
        }


//        if (convertView == null) {
//            if (itemViewType == Constans.MessageStatus.FROM) {//问题发送方
//                convertView = LayoutInflater.from(context).inflate(R.layout.item_robot_send, parent, false);
//                myViewHolder1 = new MyViewHolder(convertView);
//                convertView.setTag(myViewHolder1);
//            } else {//回答问题方（机器人）
//                convertView = LayoutInflater.from(context).inflate(R.layout.item_robot, parent, false);
//                myViewHolder = new MyViewHolder(convertView);
//                convertView.setTag(myViewHolder);
//            }
//        } else {
//            if (itemViewType == Constans.MessageStatus.FROM) {
//                myViewHolder1 = (MyViewHolder) convertView.getTag();
//            } else {
//                myViewHolder = (MyViewHolder) convertView.getTag();
//            }
////            myViewHolder = (MyViewHolder) convertView.getTag();
//        }
//
//
//        long time = messageBean.getTime();
//        String content = messageBean.getContent();
//        String photo = messageBean.getPhoto();

//        if (Constans.MessageStatus.FROM == status) {
//            if (time != 0) {
//                myViewHolder1.tvTime.setText(Utils.getDateToString(time));
//            }
//            if (!TextUtils.isEmpty(content)) {
//                myViewHolder1.tvContent.setText(content);
//            }
//            if (!TextUtils.isEmpty(photo)) {
//                ImageLoadUtils.loadCircle(context, photo, myViewHolder1.ivPhoto);
//            } else {
//                ImageLoadUtils.loadCircle(context, R.mipmap.ic_launcher, myViewHolder1.ivPhoto);
//            }
//
//        } else {
//            if (time != 0) {
//                myViewHolder.tvTime.setText(Utils.getDateToString(time));
//            }
//            if (!TextUtils.isEmpty(content)) {
//                myViewHolder.tvContent.setText(content);
//            }
//            if (!TextUtils.isEmpty(photo)) {
//                ImageLoadUtils.loadCircle(context, photo, myViewHolder.ivPhoto);
//            } else {
////                if (FROM == status) {//发送方
////                    ImageLoadUtils.loadCircle(context, R.mipmap.ic_launcher, myViewHolder.ivPhoto);
////                } else {//接收方，也就是机器人
////                    ImageLoadUtils.loadCircle(context, R.mipmap.jqr, myViewHolder.ivPhoto);
////                }
//                ImageLoadUtils.loadCircle(context, R.mipmap.jqr, myViewHolder.ivPhoto);
//            }
//        }


        return convertView;
    }

    private static class MyViewHolder {
        View itemView;
        private ImageView ivPhoto;
        private TextView tvContent;
        private TextView tvTime;

        public MyViewHolder(View itemView) {
            this.itemView = itemView;

            ivPhoto = (ImageView) itemView.findViewById(R.id.iv_photo);
            tvContent = (TextView) itemView.findViewById(R.id.tv_content);
            tvTime = (TextView) itemView.findViewById(R.id.tv_time);
        }
    }
}
