import java.util.Random;

public class Join {
    public static void main(String args[])
    {
        Object lock = new Object();
        Thread[] eArray = new Thread[1000000];
        for (int i=0;i<1000000;i++) {
            eArray[i]=new EachSyncThread("e"+i, lock);
            eArray[i].start();
            // join可以同步线程
            // A线程中调用B线程的join方法时，内部原理是循环执行B.wait(now-base(>0))  直到timeout之后推出循环继续并发A、B线程
            //    若发生B在定时范围内运行结束，B会自动调用notifyall , 这时即使没有达到限定时间，A也会获得锁然后继续执行。
            // 在线程数多的时候notifyAll效率很低，若同步执行，只需要通知执行线程即可，需要一套“生产订阅”关系以解决这个问题。
            try {
                eArray[i].join((new Random()).nextInt(1000)+10000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}

class EachSyncThread extends Thread {
    Object o ;
    EachSyncThread(String name, Object o)
    {
        super(name);
        this.o=o;
    }
    @Override
    public void run() {
        synchronized(o){
            System.out.println(this.getName()+" going done!");
        }

    }
}
