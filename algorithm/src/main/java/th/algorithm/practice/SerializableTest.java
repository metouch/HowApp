package th.algorithm.practice;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.HashMap;

/**
 * Created by me_touch on 18-4-12.
 *
 */

public class SerializableTest implements Serializable{

    private B b;

    private int num;

    HashMap<String, B> map = new HashMap<>();

    public SerializableTest(){
        this.b = new B();
    }

    public void setNum(int num) {
        this.num = num;
    }

    public void setB(int value) {
        this.b.setB(value);
        this.b.setA(value + 1);
        System.out.println(b.getB() + b.getA());
    }

    public void setMap() {
        B b = new B();
        b.setB(6);
        map.put("B", b);
    }

    public int getNum() {
        return num;
    }

    public void trySerialB(){
        try {
            File file = new File("algorithm/B.tmp");
            if(file.exists())
                file.delete();
            file.createNewFile();
            ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(file));
            oos.writeObject(b);
            oos.flush();
            oos.close();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void tryDeSerialB(){
        try {
            File file = new File("algorithm/B.tmp");
            if(file.exists()) {
                ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file));
                B b = (B)ois.readObject();
                System.out.println(b.getB() + b.getA());
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void trySerial(){
        try {
            File file = new File("algorithm/files/serialization.tmp");
            if(file.exists())
                file.delete();
            file.createNewFile();
            ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(file));
            oos.writeObject(this);
            oos.flush();
            oos.close();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public static class B extends A{

        private int b;

        public void setB(int b) {
            this.b = b;
        }

        public int getB() {
            return b;
        }
    }

    public static class A implements Serializable{
        private int a;

        public A(){
            this.a = 0;
        }

        public void setA(int a) {
            this.a = a;
        }

        public int getA() {
            return a;
        }
    }
}
