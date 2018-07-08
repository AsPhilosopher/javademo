package com.plain.zookeeper.curator.master;

import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.leader.LeaderSelector;
import org.apache.curator.framework.recipes.leader.LeaderSelectorListener;
import org.apache.curator.framework.state.ConnectionState;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.curator.utils.CloseableUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 轮流当leader
 */
public class LeaderSelectorTest {
    private static final String PATH = "/demo/leader";
    private static final String CONNECT_ADDR = "127.0.0.1:2181,127.0.0.1:2182,127.0.0.1:2183";

    public static void main(String[] args) {

        List<LeaderSelector> selectors = new ArrayList<>();
        List<CuratorFramework> clients = new ArrayList<>();
        try {
            for (int i = 0; i < 10; i++) {
                CuratorFramework client = getClient();
                clients.add(client);

                final String name = "client#" + i;
                LeaderSelector leaderSelector = new LeaderSelector(client, PATH, new LeaderSelectorListener() {
                    @Override
                    public void takeLeadership(CuratorFramework client) throws Exception {
                        System.out.println(name + ":I am leader.");
                        Thread.sleep(2000);
                    }

                    @Override
                    public void stateChanged(CuratorFramework client, ConnectionState newState) {
                        System.out.println(name + ":I change to " + newState.name());
                    }
                });

                //autoRequeue方法，确保此实例在释放领导权后还可能获得领导权
                leaderSelector.autoRequeue();
                leaderSelector.start();
                selectors.add(leaderSelector);
            }
            Thread.sleep(Integer.MAX_VALUE);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            for (LeaderSelector selector : selectors) {
                System.out.println("hasLeadership:" + selector.hasLeadership());
                CloseableUtils.closeQuietly(selector);
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
