package com.plain.zookeeper.curator.cache;

import com.plain.zookeeper.Statics;
import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.cache.TreeCache;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.CreateMode;

/**
 * 既可以监听节点的状态，又可以监听子节点的状态。类似于上面两种Cache的组合。
 */
public class TreeCacheTest {
    public static void main(String[] args) throws Exception {
        RetryPolicy policy = new ExponentialBackoffRetry(1000, 10);
        CuratorFramework curator = CuratorFrameworkFactory.builder().connectString(Statics.CONNECT_ADDR).sessionTimeoutMs(Statics.SESSION_TIMEOUT)
                .retryPolicy(policy).build();
        curator.start();

        TreeCache treeCache = new TreeCache(curator, "/treeCache");
        treeCache.start();

        treeCache.getListenable().addListener((curatorFramework, treeCacheEvent) -> {
            switch (treeCacheEvent.getType()) {
                case NODE_ADDED:
                    System.out.println("NODE_ADDED：路径：" + treeCacheEvent.getData().getPath() + "，数据：" + new String(treeCacheEvent.getData().getData())
                            + "，状态：" + treeCacheEvent.getData().getStat());
                    break;
                case NODE_UPDATED:
                    System.out.println("NODE_UPDATED：路径：" + treeCacheEvent.getData().getPath() + "，数据：" + new String(treeCacheEvent.getData().getData())
                            + "，状态：" + treeCacheEvent.getData().getStat());
                    break;
                case NODE_REMOVED:
                    System.out.println("NODE_REMOVED：路径：" + treeCacheEvent.getData().getPath() + "，数据：" + new String(treeCacheEvent.getData().getData())
                            + "，状态：" + treeCacheEvent.getData().getStat());
                    break;
                default:
                    break;
            }
        });

        curator.create().forPath("/treeCache", "123".getBytes());
        curator.create().creatingParentsIfNeeded().withMode(CreateMode.PERSISTENT).forPath("/treeCache/c1", "456".getBytes());
        Thread.sleep(1000);
        curator.setData().forPath("/treeCache", "789".getBytes());
        curator.setData().forPath("/treeCache/c1", "910".getBytes());
        Thread.sleep(1000);
        curator.delete().forPath("/treeCache/c1");
        curator.delete().forPath("/treeCache");
        Thread.sleep(5000);
        curator.close();
    }
}
