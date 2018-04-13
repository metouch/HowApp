package com.robot;

/**
 * Created by Administrator on 2017-11-06.
 * 机器人聊天的实体
 */

public class MessageBean {
    private int id;
    private int type = 1; //1为文字, 2为语音
    private int showType = 1; //1为文字, 2为语音
    private String content;
    private String photo;
    private String recordFilePath;
    private long time;
    private long newsId = -1;
    private int newsType = -1;

    private int status;//是发送方还是接收方

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public void setRecordFilePath(String recordFilePath) {
        this.recordFilePath = recordFilePath;
    }

    public String getRecordFilePath() {
        return recordFilePath;
    }

    public void setType(int type) {
        this.type = type;
    }

    public void setShowType(int showType) {
        this.showType = showType;
    }

    public int getShowType() {
        return showType;
    }

    public int getType() {
        return type;
    }

    public void setNewsId(long newsId) {
        this.newsId = newsId;
    }

    public long getNewsId() {
        return newsId;
    }

    public void setNewsType(int newsType) {
        this.newsType = newsType;
    }

    public int getNewsType() {
        return newsType;
    }

    @Override
    public String toString() {
        return "MessageBean{" +
                "id=" + id +
                ", type=" + type +
                ", showType=" + showType +
                ", content='" + content + '\'' +
                ", photo='" + photo + '\'' +
                ", recordFilePath='" + recordFilePath + '\'' +
                ", time=" + time +
                ", status=" + status +
                '}';
    }
}
