package th.how.bean;

import java.io.Serializable;

/**
 * Created by me_touch on 17-9-29.
 *
 */

public class StudentEntity implements Serializable{

    //学号
    int number;
    String name;

    public StudentEntity(int number, String name){
        this.number = number;
        this.name = name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    @Override
    public String toString() {
        return "number = " + number + ", name = " + name;
    }
}
