package th.algorithm.serial;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.ObjectStreamException;
import java.io.Serializable;

/**
 * Created by me_touch on 18-4-18.
 *
 */

public class SerializableReplaceTest {

    private void trySerialInstance(){
        try {
            System.out.println(SingleInstance.INSTANCE);
            File file = new File("algorithm/files/instance.tmp");
            if(file.exists())
                file.delete();
            file.createNewFile();
            ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(file));
            oos.writeObject(SingleInstance.INSTANCE);
            oos.flush();
            oos.close();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void tryDeSerialInstance(){
        try {
            File file = new File("algorithm/files/instance.tmp");
            if(file.exists()) {
                ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file));
                Object object = ois.readObject();
                System.out.println(object);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void trySerialChild(){
        Child replace = new Child();
        replace.setA(9);
        try {
            File file = new File("algorithm/files/child.tmp");
            if(file.exists())
                file.delete();
            file.createNewFile();
            ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(file));
            oos.writeObject(replace);
            oos.flush();
            oos.close();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void tryDeSerialChild(){
        try {
            File file = new File("algorithm/files/c.tmp");
            if(file.exists()) {
                ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file));
                Object object = ois.readObject();
                System.out.println(object);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void trySerial(){
        WriteReplace replace = new WriteReplace();
        replace.setA(9);
        try {
            File file = new File("algorithm/files/c.tmp");
            if(file.exists())
                file.delete();
            file.createNewFile();
            ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(file));
            oos.writeObject(replace);
            oos.flush();
            oos.close();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void tryDeSerial(){
        try {
            File file = new File("algorithm/files/c.tmp");
            if(file.exists()) {
                ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file));
                Object object = ois.readObject();
                System.out.println(object);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public static void main(String[] args){
        SerializableReplaceTest test = new SerializableReplaceTest();
        test.trySerial();
        test.tryDeSerial();
        test.trySerialChild();
        test.tryDeSerialChild();
        test.trySerialInstance();
        test.tryDeSerialInstance();
    }

    public static class Child extends WriteReplace{

    }

    public static class WriteReplace implements Serializable{
        private int a;

        public void setA(int a) {
            this.a = a;
        }

        public int getA() {
            return a;
        }

        private Object writeReplace() throws ObjectStreamException{
            return new Common();
        }
    }

    public static class Common implements Serializable{

        private transient int a;

        public  Common(){
            this.a = 1;
        }

        public int getA() {
            return a;
        }

        private void writeObject(java.io.ObjectOutputStream out) throws IOException {
            out.defaultWriteObject();
            out.writeInt(a);
        }

        private void readObject(java.io.ObjectInputStream in) throws IOException, ClassNotFoundException{
            in.defaultReadObject();
            in.readInt();
            System.out.println("Common readObject has been invoked");
        }

    }

    public static class SingleInstance implements Serializable{

        public static SingleInstance INSTANCE = new SingleInstance();

        private transient int a;

        private  SingleInstance(){
            this.a = 1;
        }

        public int getA() {
            return a;
        }

        private void writeObject(java.io.ObjectOutputStream out) throws IOException {
            out.defaultWriteObject();
            out.writeInt(a);
        }

        private void readObject(java.io.ObjectInputStream in) throws IOException, ClassNotFoundException{
            in.defaultReadObject();
            in.readInt();
            System.out.println("SingleInstance readObject has been invoked");
        }

        private Object readResolve() throws ObjectStreamException{
            return INSTANCE;
        }
    }
}
