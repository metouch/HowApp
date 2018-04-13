package th.algorithm;

import java.io.File;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Consumer;

import th.algorithm.practice.SerializableTest;
import th.algorithm.practice.Server;
import th.algorithm.practice.SolutionUtils;

public class MainClass {

    private static List<String> list = new ArrayList<>();

    public static void main(String[] args){
//        SolutionUtils.valueOfYHTriAngle(10);
//        int[] arr = new int[]{0, 0, 0, 0, 1, 0, 1, 0, 0, 0, 1, 0, 1, 0, 0, 0, 1};
//        System.out.println(SolutionUtils.zeo2one(arr, 4));
//        try{
//            FileOutputStream out = new FileOutputStream(FileDescriptor.out);
//            out.write('a');
//            out.close();
//        }catch(Exception e){
//            e.printStackTrace();
//        }
 //       new Server();
        SerializableTest test = new SerializableTest();
        test.setB(5);
        test.trySerialB();
        test.tryDeSerialB();
//        test.setMap();
//        test.trySerial();
//        try {
//            File file = new File("algorithm/files/serialization.tmp");
//            ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file));
//            SerializableTest test1 = (SerializableTest)ois.readObject();
//            System.out.println(test1.getA());
//
//        }catch (Exception e){
//            e.printStackTrace();
//        }



    }

    
}
