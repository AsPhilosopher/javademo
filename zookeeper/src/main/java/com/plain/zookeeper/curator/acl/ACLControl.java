package com.plain.zookeeper.curator.acl;

import com.plain.zookeeper.Statics;
import org.apache.curator.framework.AuthInfo;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.api.ACLProvider;
import org.apache.curator.retry.RetryNTimes;
import org.apache.zookeeper.ZooDefs;
import org.apache.zookeeper.data.ACL;
import org.apache.zookeeper.data.Id;
import org.apache.zookeeper.server.auth.DigestAuthenticationProvider;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class ACLControl {
    private String authStr1 = "admin:admin";
    private String authStr2 = "user:user";

    private String scheme1 = "digest";
    private byte[] auth1 = authStr1.getBytes();
    private String namespace1 = "testnamespace";

    private byte[] auth2 = authStr2.getBytes();

    @Test
    public void test1() throws Exception {
//        设置权限访问类型
//        TODO 下面两种情况
//        ACLProvider aclProvider = getACLProvider1("all");
        ACLProvider aclProvider = getACLProvider1("read");
        CuratorFramework curator1 = getCuratorFramework1(scheme1, auth1, namespace1, aclProvider);
        curator1.start();

        try {
            curator1.create().creatingParentsIfNeeded().forPath("/test", "create init !".getBytes());
            System.out.println(curator1.getACL().forPath("/"));
            System.out.println(curator1.getACL().forPath("/test"));
            curator1.setData().forPath("/test", "hello".getBytes());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            curator1.delete().forPath("/test");
        }
    }

    @Test
    public void test2() throws Exception {
//        设置权限访问类型
//        TODO 下面两种情况
        ACLProvider aclProvider = getACLProvider1("all");
//        ACLProvider aclProvider = getACLProvider1("read");
        CuratorFramework curator1 = getCuratorFramework1(scheme1, auth1, namespace1, aclProvider);
        curator1.start();

        curator1.create().creatingParentsIfNeeded().forPath("/test", "create init !".getBytes());
        System.out.println(curator1.getACL().forPath("/"));
        System.out.println(curator1.getACL().forPath("/test"));

        CuratorFramework curator2 = getCuratorFramework1(scheme1, auth2, namespace1, aclProvider);
        curator2.start();
        try {
            curator2.setData().forPath("/test", "hello".getBytes());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            curator1.delete().forPath("/test");
        }
    }

    @Test
    public void test3() throws Exception {
//        设置权限访问类型
//        TODO 下面两种情况
        ACLProvider aclProvider = getACLProvider1("all");
//        ACLProvider aclProvider = getACLProvider1("read");
        List<AuthInfo> authInfoList = new ArrayList<>();
        authInfoList.add(new AuthInfo(scheme1, auth1));
        authInfoList.add(new AuthInfo(scheme1, auth2));
        CuratorFramework curator1 = getCuratorFramework2(authInfoList, namespace1, aclProvider);
        curator1.start();

        curator1.create().creatingParentsIfNeeded().forPath("/test", "create init !".getBytes());
        System.out.println(curator1.getACL().forPath("/"));
        System.out.println(curator1.getACL().forPath("/test"));

        CuratorFramework curator2 = getCuratorFramework1(scheme1, auth2, namespace1, aclProvider);
        curator2.start();
        try {
            curator2.setData().forPath("/test", "hello".getBytes());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            curator1.delete().forPath("/test");
        }
    }

    /**
     * 验证在ACLProvider里设置id的值是否有用
     *
     * @throws Exception
     */
    @Test
    public void test4() throws Exception {
//        设置权限访问类型
//        TODO 下面两种情况
        ACLProvider aclProvider = getACLProvider2("all", authStr2);
//        ACLProvider aclProvider = getACLProvider2("read", authStr1);
        CuratorFramework curator1 = getCuratorFramework1(scheme1, auth1, namespace1, aclProvider);
        curator1.start();

        curator1.create().creatingParentsIfNeeded().forPath("/test", "create init !".getBytes());
        System.out.println(curator1.getACL().forPath("/"));
        System.out.println(curator1.getACL().forPath("/test"));

        CuratorFramework curator2 = getCuratorFramework1(scheme1, auth2, namespace1, aclProvider);
        curator2.start();
        try {
            curator2.setData().forPath("/test", "hello".getBytes());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            curator1.delete().forPath("/test");
        }
    }

    /**
     * 动态添加有权限的用户
     *
     * @throws Exception
     */
    @Test
    public void test5() throws Exception {
//        设置权限访问类型
//        TODO 下面两种情况
        ACLProvider aclProvider = getACLProvider1("all");
//        ACLProvider aclProvider = getACLProvider1("read");
        CuratorFramework curator1 = getCuratorFramework1(scheme1, auth1, namespace1, aclProvider);
        curator1.start();

        curator1.create().creatingParentsIfNeeded().forPath("/test", "create init !".getBytes());
        List<ACL> aclList = curator1.getACL().forPath("/test");
        aclList.add(new ACL(ZooDefs.Perms.ALL, new Id("digest", DigestAuthenticationProvider.generateDigest("user:user"))));
        curator1.setACL().withACL(aclList).forPath("/test");
        System.out.println(curator1.getACL().forPath("/"));
        System.out.println(curator1.getACL().forPath("/test"));

        CuratorFramework curator2 = getCuratorFramework1(scheme1, auth2, namespace1, aclProvider);
        curator2.start();
        try {
            curator2.setData().forPath("/test", "hello".getBytes());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            curator1.delete().forPath("/test");
        }
    }

    private ACLProvider getACLProvider1(String type) {
        return new ACLProvider() {
            private List<ACL> acl = null;

            @Override
            public List<ACL> getDefaultAcl() {
                if (acl == null) {
                    ArrayList<ACL> acl = new ArrayList<>();
                    if ("read".equals(type)) {
                        acl.add(new ACL(ZooDefs.Perms.READ, new Id("auth", "")));
                    } else {
                        acl.add(new ACL(ZooDefs.Perms.ALL, new Id("auth", "")));
                    }
                    this.acl = acl;
                }
                return acl;
            }

            @Override
            public List<ACL> getAclForPath(String path) {
                return acl;
            }
        };
    }

    private ACLProvider getACLProvider2(String type, String id) {
        return new ACLProvider() {
            private List<ACL> acl = null;

            @Override
            public List<ACL> getDefaultAcl() {
                if (acl == null) {
                    ArrayList<ACL> acl = new ArrayList<>();
                    if ("read".equals(type)) {
                        acl.add(new ACL(ZooDefs.Perms.READ, new Id("auth", id)));
                    } else {
                        acl.add(new ACL(ZooDefs.Perms.ALL, new Id("auth", id)));
                    }
                    this.acl = acl;
                }
                return acl;
            }

            @Override
            public List<ACL> getAclForPath(String path) {
                return acl;
            }
        };
    }

    private CuratorFramework getCuratorFramework1(String scheme, byte[] auth, String namespace, ACLProvider aclProvider) {
        return CuratorFrameworkFactory.builder().aclProvider(aclProvider).
                authorization(scheme, auth).
                connectionTimeoutMs(Statics.SESSION_TIMEOUT).
                connectString(Statics.CONNECT_ADDR).
                namespace(namespace).
                retryPolicy(new RetryNTimes(Integer.MAX_VALUE, 1000)).build();
    }

    private CuratorFramework getCuratorFramework2(List<AuthInfo> authInfoList, String namespace, ACLProvider aclProvider) {
        return CuratorFrameworkFactory.builder().aclProvider(aclProvider).
                authorization(authInfoList).
                connectionTimeoutMs(Statics.SESSION_TIMEOUT).
                connectString(Statics.CONNECT_ADDR).
                namespace(namespace).
                retryPolicy(new RetryNTimes(Integer.MAX_VALUE, 1000)).build();
    }
}
