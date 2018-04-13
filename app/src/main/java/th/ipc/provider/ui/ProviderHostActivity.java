package th.ipc.provider.ui;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import th.how.app.R;

public class ProviderHostActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_provider_host);
        TestA o = new TestA();
        ThreadA a = new ThreadA(o);
        ThreadB b = new ThreadB(o);
        ThreadC c = new ThreadC(o);
        ThreadD d = new ThreadD(o);
        ThreadE e = new ThreadE(o);
        a.start();
        b.start();
        c.start();
        d.start();
    }

    public static class TestA{

        public synchronized void add(){
            int num = 0;
            while (num < 5){
                System.out.println(Thread.currentThread().getName() + ", num = "  + num);
                try {
                    Thread.sleep(500);
                }catch (Exception e){
                    e.printStackTrace();
                }
                num ++;
            }
        }
        public synchronized void addB(){
            int count = 0;
            while (count < 5){
                System.out.println(Thread.currentThread().getName() + ", count = "  + count);
                try {
                    Thread.sleep(500);
                }catch (Exception e){
                    e.printStackTrace();
                }
                count ++;
            }
        }
        public synchronized void addC(){
            int value = 0;
            while (value < 5){
                System.out.println(Thread.currentThread().getName() + ", value = "  + value);
                try {
                    Thread.sleep(500);
                }catch (Exception e){
                    e.printStackTrace();
                }
                value ++;
            }
        }
    }

    public static class ThreadA extends Thread{
        TestA o;
        public ThreadA(TestA o){
            this.o =o;
        }

        @Override
        public void run() {
            super.run();
            o.add();
        }
    }

    public static class ThreadB extends Thread{
        TestA o;
        public ThreadB(TestA o){
            this.o =o;
        }

        @Override
        public void run() {
            super.run();
            o.add();
        }
    }

    public static class ThreadC extends Thread{
        TestA o;
        public ThreadC(TestA o){
            this.o =o;
        }

        @Override
        public void run() {
            super.run();
            o.add();
        }
    }

    public static class ThreadD extends Thread{
        TestA o;
        public ThreadD(TestA o){
            this.o =o;
        }

        @Override
        public void run() {
            super.run();
            o.add();
            o.addB();
            o.addC();
        }
    }

    public static class ThreadE extends Thread{
        final TestA o;
        public ThreadE(final TestA o){
            this.o =o;
        }

        @Override
        public void run() {
            super.run();
            synchronized (o){
                o.add();
                o.addB();
                o.addC();
            }

        }
    }
}
