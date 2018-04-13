package th.how.bean;

import android.os.Parcel;
import android.os.Parcelable;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Keep;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by me_touch on 2017/8/16.
 *
 */

@Entity
public class NewsEntity implements Parcelable{

    @Id
    private long id;
    private String title;
    private String subTitle;

    public NewsEntity(Parcel in){
        readFromParcel(in);
    }

    @Keep
    public NewsEntity(long id, String title){
        this.id = id;
        this.title = title;
    }

    @Generated(hash = 2121778047)
    public NewsEntity() {
    }

    @Generated(hash = 1344646624)
    public NewsEntity(long id, String title, String subTitle) {
        this.id = id;
        this.title = title;
        this.subTitle = subTitle;
    }

    public void setId(int id) {
        this.id = id;
    }

    public long getId() {
        return id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    public void setSubTitle(String subTitle) {
        this.subTitle = subTitle;
    }

    public String getSubTitle() {
        return subTitle;
    }

    @Override
    public String toString() {
        return "id = " + id + ", title = " + title + ", subTitle = " + subTitle;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(id);
        dest.writeString(title);
        dest.writeString(subTitle);
    }

    public void readFromParcel(Parcel in){
        this.id = in.readLong();
        this.title = in.readString();
        this.subTitle = in.readString();
    }

    public void setId(long id) {
        this.id = id;
    }
    public static final Creator<NewsEntity> CREATOR = new Creator<NewsEntity>() {
        @Override
        public NewsEntity createFromParcel(Parcel source) {
            return new NewsEntity(source);
        }

        @Override
        public NewsEntity[] newArray(int size) {
            return new NewsEntity[size];
        }
    };
}
