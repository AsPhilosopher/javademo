package com.plain.zookeeper.curator.master;

import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.leader.LeaderLatch;
import org.apache.curator.framework.recipes.leader.LeaderLatchListener;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.curator.utils.CloseableUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 随机从候选着中选出一台作为leader，选中之后除非调用close()释放leadship，否则其他的后选择无法成为leader。其中spark使用的就是这种方法。
 */
public class LeaderLatchTest {
    private static final String PATH = "/demo/leader";
    private static final String CONNECT_ADDR = "127.0.0.1:2181,127.0.0.1:2182,127.0.0.1:2183";

    public static void main(String[] args) {
        List<LeaderLatch> latchList = new ArrayList<>();
        List<CuratorFramework> clients = new ArrayList<>();
        try {
            for (int i = 0; i < 10; i++) {
                CuratorFramework client = getClient();
                clients.add(client);

                final LeaderLatch leaderLatch = new LeaderLatch(client, PATH, "client#" + i);
                leaderLatch.addListener(new LeaderLatchListener() {
                    @Override
                    public void isLeader() {
                        System.out.println(leaderLatch.getId() + ":I am leader. I am doing jobs!");
                    }

                    @Override
                    public void notLeader() {
                        System.out.println(leaderLatch.getId() + ":I am not leader. I will do nothing!");
                    }
                });
                latchList.add(leaderLatch);
                leaderLatch.start();
            }
            Thread.sleep(3000);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            for (LeaderLatch leaderLatch : latchList) {
                System.out.println("hasLeadership:" + leaderLatch.hasLeadership());
                CloseableUtils.closeQuietly(leaderLatch);
            }

            for (CuratorFramework client : clients) {
                CloseableUtils.closeQuietly(client);
            }
        }

    }

    private static CuratorFramework getClient() {
        RetryPolicy retryPolicy = new ExponentialBackoffRetry(1000, 3);
        CuratorFramework client = CuratorFrameworkFactory.builder()
                .connectString(CONNECT_ADDR)
                .retryPolicy(retryPolicy)
                .sessionTimeoutMs(6000)
                .connectionTimeoutMs(3000)
                .namespace("demo")
                .build();
        client.start();
        return client;
    }
}
