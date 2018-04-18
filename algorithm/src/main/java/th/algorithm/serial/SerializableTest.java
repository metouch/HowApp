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
 * 如何测试 readObjectNoData 方法?
 * =========================================================
 * 序列化时:
 * 需要注释掉 与 field c相关的 field 及method,
 * 并不实现 A 的序列化, 然后在 main 函数中实现 B 对象的序列化.
 * =========================================================
 * 反序列化时:
 * 恢复被你注释掉的代码, 并实现 A 的序列化, 然后在 main 函数中
 * 注释掉代码序列化相关, 并执行反序列化方法
 *
 */

public class SerializableTest implements Serializable{

    private B b;

    public SerializableTest(){
        this.b = new B();
    }

    public void setB(int value) {
        this.b.setB(value);
        this.b.setA(value + 1);
        System.out.println(b.getB() + b.getA());
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
                System.out.println(b.getB() + b.getC());
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public static void main(String[] args){
        SerializableTest test = new SerializableTest();
        // 序列化B
//        test.setB(5);
//        test.trySerialB();
        //反序列化B
        test.tryDeSerialB();
    }

    public static class B extends A implements Serializable{

        private int b;

        public B(){
        }

        public B(int a){
        }

        public void setB(int b) {
            this.b = b;
        }

        public int getB() {
            return b;
        }

        private void writeObject(java.io.ObjectOutputStream out) throws IOException {
            out.defaultWriteObject();
        }

        private void readObject(java.io.ObjectInputStream in) throws IOException, ClassNotFoundException{
            in.defaultReadObject();
        }

    }

    public static class A implements Serializable{

        private int a;
        private int c;

        /**
         * 若想子类可序列化, 则必须存在子类可访问的无参构造函数
         */
        A(){
           this.a = 0;
        }

        /**
         * 不能使用 private 修饰, 对子类不可访问
        private A(){

        }

         */
        public A(int a){
            this.a = a;
        }

        public void setA(int a) {
            this.a = a;
        }

        public int getA() {
            return a;
        }

        public void setC(int c) {
            this.c = c;
        }

        public int getC() {
            return c;
        }

        private void readObjectNoData()throws ObjectStreamException{
            setA(6);
            setC(8);
            System.out.println("readObjectNoData has been invoked");
        }
    }
}
