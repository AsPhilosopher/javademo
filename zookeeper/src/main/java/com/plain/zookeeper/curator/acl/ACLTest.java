package com.plain.zookeeper.curator.acl;

import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.ZooDefs;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.data.ACL;
import org.apache.zookeeper.data.Id;
import org.apache.zookeeper.server.auth.DigestAuthenticationProvider;
import org.junit.Test;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class ACLTest {
    @Test
    public void test() throws IOException, KeeperException, InterruptedException {
        ZooKeeper zooKeeper1 = new ZooKeeper("10.45.4.57:2181", 5000, null);

        /**
         * 自定义aclList
         */
        /*List<ACL> aclList = new ArrayList<>();
        Id id = new Id("ip", "192.168.0.0");
        ACL acl = new ACL(ZooDefs.Perms.READ | ZooDefs.Perms.WRITE, id);
        aclList.add(acl);
        zooKeeper1.create("/test", "init".getBytes(), aclList, CreateMode.EPHEMERAL);*/

        /**
         * 根据用户的权限，设置节点权限
         */
        zooKeeper1.addAuthInfo("digest", "foo:true".getBytes());
        zooKeeper1.create("/test", "init".getBytes(), ZooDefs.Ids.CREATOR_ALL_ACL, CreateMode.EPHEMERAL);


        ZooKeeper zooKeeper2 = new ZooKeeper("10.45.4.57:2181", 5000, null);
        zooKeeper2.addAuthInfo("digest", "xxxx:xxxx_admin".getBytes());
        System.out.println("Admin: " + new String(zooKeeper2.getData("/test", null, null)));

        ZooKeeper zooKeeper3 = new ZooKeeper("10.45.4.57:2181", 5000, null);
        zooKeeper3.addAuthInfo("digest", "aaa:bbb".getBytes());
        System.out.println(new String(zooKeeper3.getData("/test", null, null)));


        TimeUnit.HOURS.sleep(24);
    }

    @Test
    public void test2() throws NoSuchAlgorithmException {
        System.out.println(DigestAuthenticationProvider.generateDigest("xxxx:xxxx_admin"));
    }
}
