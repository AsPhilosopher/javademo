package com.plain.zookeeper.curator.lock;

import com.plain.zookeeper.Statics;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.locks.InterProcessSemaphoreMutex;
import org.apache.curator.retry.ExponentialBackoffRetry;

public class SharedNoReentrantLock implements Runnable {
    private InterProcessSemaphoreMutex lock;//不可重入锁
    private String lockPAth = "/lock/shareLock";
    private static int I = 0;
    private String clientName;

    public SharedNoReentrantLock(CuratorFramework client, String clientName) {
        lock = new InterProcessSemaphoreMutex(client, lockPAth);
        this.clientName = clientName;
    }

    public void run() {
        try {
            Thread.sleep((new java.util.Random().nextInt(2000)));
            lock.acquire();  //增加第一把锁
            if (lock.isAcquiredInThisProcess()) {
                System.out.println(clientName + " 获得锁");
                I++;
            } else {
                System.out.println("未获取锁");
            }
//            lock.acquire();  //增加第二把锁则会阻塞
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                System.out.println(clientName + "释放第一把锁");
                lock.release();
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }

    }

    public static void main(String[] args) throws Exception {
        CuratorFramework client = CuratorFrameworkFactory.newClient(Statics.CONNECT_ADDR, new ExponentialBackoffRetry(1000, 3));
        client.start();
//        client.delete().guaranteed().deletingChildrenIfNeeded().forPath("/lock/shareLock");
        for (int i = 0; i < 100; i++) {
            SharedNoReentrantLock sharedNoReentrantLock = new SharedNoReentrantLock(client, "第" + i + "个客户端：");
            Thread thread = new Thread(sharedNoReentrantLock);
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
