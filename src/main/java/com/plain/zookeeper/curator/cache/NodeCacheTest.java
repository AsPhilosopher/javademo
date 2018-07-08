package com.plain.zookeeper.curator.cache;

import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.cache.NodeCache;
import org.apache.curator.retry.ExponentialBackoffRetry;

/**
 * 监听节点的新增、修改，删除操作。
 */
public class NodeCacheTest {
    private static final String CONNECT_ADDR = "127.0.0.1:2181,127.0.0.1:2182,127.0.0.1:2183";
    private static final int SESSION_TIMEOUT = 5000;

    public static void main(String[] args) throws Exception {
        RetryPolicy policy = new ExponentialBackoffRetry(1000, 10);
        CuratorFramework curator = CuratorFrameworkFactory.builder().connectString(CONNECT_ADDR)
                .sessionTimeoutMs(SESSION_TIMEOUT).retryPolicy(policy).build();
        curator.start();

        //最后一个参数表示是否进行压缩
        NodeCache cache = new NodeCache(curator, "/supers", false);
        cache.start(true);

        //会监听节点的创建，修改和删除
        cache.getListenable().addListener(() -> {
            if (null != cache.getCurrentData()) {
                System.out.println("路径：" + cache.getCurrentData().getPath());
                System.out.println("数据：" + new String(cache.getCurrentData().getData()));
                System.out.println("状态：" + cache.getCurrentData().getStat());
            } else {
                System.out.println("Data is null");
            }
        });

        curator.create().forPath("/supers", "1234".getBytes());
        Thread.sleep(1000);
        curator.setData().forPath("/supers", "5678".getBytes());
        Thread.sleep(1000);
        curator.delete().forPath("/supers");
        Thread.sleep(5000);
        curator.create().creatingParentsIfNeeded().forPath("/supers/aa");
        Thread.sleep(1000);
        curator.delete().guaranteed().deletingChildrenIfNeeded().forPath("/supers");
        curator.close();
    }
}
