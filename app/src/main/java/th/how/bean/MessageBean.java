package th.how.bean;

/**
 * Created by Administrator on 2017-11-06.
 * 机器人聊天的实体
 */

public class MessageBean {
    private int id;
    private String content;
    private String photo;
    private long time;

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


    @Override
    public String toString() {
        return "MessageBean{" +
                "id=" + id +
                ", content='" + content + '\'' +
                ", photo='" + photo + '\'' +
                ", time=" + time +
                ", status=" + status +
                '}';
    }
}
