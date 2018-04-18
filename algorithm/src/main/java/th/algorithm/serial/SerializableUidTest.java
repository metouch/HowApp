package th.algorithm.serial;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

/**
 * ===================================================
 * 首先使用现有代码生成一个序列化后的文件
 * 然后恢复注释掉的部分(忽略掉手动指定uid的那部分代码)
 * , 并注释掉 main函数中
 * 序列化的部分, 并保留反序列化的部分, 运行.
 * ====================================================
 */

public class SerializableUidTest {

    private void trySerial(){
        try {
            File file = new File("algorithm/files/uid.tmp");
            if(file.exists())
                file.delete();
            file.createNewFile();
            ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(file));
            oos.writeObject(new Uid());
            oos.flush();
            oos.close();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void tryDeSerial(){
        try {
            File file = new File("algorithm/files/uid.tmp");
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
        SerializableUidTest test = new SerializableUidTest();
//        test.trySerial();
        test.tryDeSerial();
    }

    public static class Uid implements Serializable{

//        private static final long serialVersionUID = 42L;

        private int a;
        private int c;

        public Uid(){
            this.a = 6;
        }
    }
}
