package com.jason.zk;

import org.apache.zookeeper.*;
import org.apache.zookeeper.data.ACL;
import org.apache.zookeeper.data.Id;
import org.apache.zookeeper.data.Stat;
import org.apache.zookeeper.server.auth.DigestAuthenticationProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by jasoncc on 21/04/17.
 * zk的一个demno
 */
public class ZKDemo {

    private static final Logger log = LoggerFactory.getLogger(ZKDemo.class);
    public static final int TIME_OUT = 5000;

    private static ZooKeeper zk;
    public static ZooKeeper connect(){
        if (zk == null){
            try {
                zk = new ZooKeeper("localhost:2181", TIME_OUT, new Watcher() {
                    public void process(WatchedEvent watchedEvent) {
//                    System.out.println("接受到通知了:类型 "+watchedEvent.getType());
                        System.out.println("接受到通知了:类型 "+watchedEvent.getType());
                    }
                });
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return zk;
    }

    public static void main(String[] args) throws KeeperException, InterruptedException, NoSuchAlgorithmException {
         ZooKeeper zk = connect();
//         update("/jason","chenchen111".getBytes());
//           getDate("/jason");
//         createAuthNode();
//        createAuth();
//        doWatcher();
        createEp();
//        getpath();
    }
public static void getpath() throws KeeperException, InterruptedException {
        List<String> subs = zk.getChildren("/eppp",true);
        for (String sub:subs){
            System.out.println(sub+"--");
        }
}
    //创建一个临时有序的节点
    public static void createEp() throws KeeperException, InterruptedException {
        if (zk.exists("/eppp",true)==null){
            zk.create("/eppp",null, ZooDefs.Ids.OPEN_ACL_UNSAFE,CreateMode.PERSISTENT);
        }
        zk.create("/eppp/sub",null, ZooDefs.Ids.OPEN_ACL_UNSAFE,CreateMode.EPHEMERAL_SEQUENTIAL);
        zk.create("/eppp/sub",null, ZooDefs.Ids.OPEN_ACL_UNSAFE,CreateMode.EPHEMERAL_SEQUENTIAL);
        zk.create("/eppp/sub",null, ZooDefs.Ids.OPEN_ACL_UNSAFE,CreateMode.EPHEMERAL_SEQUENTIAL);
        List<String> subs = zk.getChildren("/eppp",true);
        for (String sub:subs){
            System.out.println(sub+"--");
        }
    }

    //实现一个监听事件
    public static void doWatcher() throws KeeperException, InterruptedException {
       // zk.create("/jjjj","jj".getBytes(),ZooDefs.Ids.OPEN_ACL_UNSAFE,CreateMode.PERSISTENT);
        zk.getData("/jjjj", new Watcher() {
            public void process(WatchedEvent watchedEvent) {
                System.out.println("值发生了变更----"+watchedEvent.getType());
                try {
                    System.out.println("path"+watchedEvent.getPath()+"--value:"+new String(zk.getData("/jjjj",true,null)));
                } catch (KeeperException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        },null);
      //  zk.setData("/jjjj","chen".getBytes(),-1);
        zk.setData("/jjjj","cc".getBytes(),-1);
    }

    public static void set(){
//        zk.setData()
    }


    //权限创建节点方式二
    public static void createAuth() throws NoSuchAlgorithmException, KeeperException, InterruptedException {
        //Schema方式有以下几种：digest ip word
        ACL acl = new ACL(ZooDefs.Perms.ALL,new Id("digest", DigestAuthenticationProvider.generateDigest("root:root")));
        List<ACL> acls = new ArrayList<ACL>();
        acls.add(acl);
        if (zk.exists("/jason/auth",null) == null){
            zk.create("/jason/auth","auth".getBytes(),acls,CreateMode.PERSISTENT);
        }
        //必须加以下代码否则没有权限访问 digest是以密码的形式的
        zk.addAuthInfo("digest","root:root".getBytes());
        System.out.println(new String(zk.getData("/jason/auth",true,null)).toString());
    }

    //权限创建节点方式一
    public static void createAuthNode() throws KeeperException, InterruptedException {
       String path =  zk.create("/jason/jasonchen","jasonchen".getBytes(),ZooDefs.Ids.READ_ACL_UNSAFE,CreateMode.PERSISTENT);
        System.out.println("path---"+path);
//        zk.delete("/jason/jasonchen",-1);
       Stat stat =  zk.setData("/jason/jasonchen","chen".getBytes(),-1);
        System.out.println(stat);
    }

    public static void delete(String path) throws KeeperException, InterruptedException {
        zk.delete("/jason",-1);
    }

    /*修改所有版本的数据
     */
    public static void update(String path,byte[] data) throws KeeperException, InterruptedException {
        Stat stat = zk.setData(path,data,-1);
        System.out.println("stat---"+stat);
    }
    public static void getDate(String path) throws KeeperException, InterruptedException {
       byte[] data =  zk.getData(path,true,null);
        System.out.println("data---"+new String(data).toString());
    }
    public void create() throws KeeperException, InterruptedException {
    String path = zk.create("/jason","jaosn".getBytes(),ZooDefs.Ids.OPEN_ACL_UNSAFE,CreateMode.PERSISTENT);
    log.info("##-----",path==null);
    }
}
