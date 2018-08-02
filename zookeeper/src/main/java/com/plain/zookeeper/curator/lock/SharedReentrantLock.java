package com.plain.zookeeper.curator.lock;

import com.plain.zookeeper.Statics;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.locks.InterProcessMutex;
import org.apache.curator.retry.ExponentialBackoffRetry;

public class SharedReentrantLock implements Runnable {
    private InterProcessMutex lock;//可重入锁实现类，类似信号量
    private String lockPAth = "/lock/shareLock";
    private static int I = 0;
    private String clientName;

    public SharedReentrantLock(CuratorFramework client, String clientName) {
        lock = new InterProcessMutex(client, lockPAth);
        this.clientName = clientName;
    }

    @Override
    public void run() {
        try {
            Thread.sleep((new java.util.Random().nextInt(2000)));
            lock.acquire();  //增加第一把锁
            if (lock.isAcquiredInThisProcess()) {
                System.out.println(clientName + " 获得锁");
                I++;
            }
            lock.acquire();  //增加第二把锁
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                System.out.println(clientName + "释放第一把锁");
                lock.release();
                System.out.println(clientName + "释放第二把锁");
                lock.release();
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }

    }

    public static void main(String[] args) throws InterruptedException {
        CuratorFramework client = CuratorFrameworkFactory.newClient(Statics.CONNECT_ADDR, new ExponentialBackoffRetry(1000, 3));
        client.start();
        //启动100个线程进行测试
        for (int i = 0; i < 100; i++) {
            SharedReentrantLock sharedReentrantLock = new SharedReentrantLock(client, "第" + i + "个客户端：");
            Thread thread = new Thread(sharedReentrantLock);
            thread.start();
        }
        while (I != 100) {
            Thread.sleep(3000);
            System.out.println("@@@:" + I);
        }
        client.close();
        System.out.println("END-END");
    }
}
