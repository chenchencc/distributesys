package com.jason.zk;

import org.apache.zookeeper.*;
import org.apache.zookeeper.data.Stat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * Created by jasoncc on 21/04/17.
 * zk的一个demno
 */
public class ZKDemo {

    private static final Logger log = LoggerFactory.getLogger(ZKDemo.class);
    public static final int TIME_OUT = 5000;
    public static ZooKeeper connect(){
        ZooKeeper zk= null;
        try {
             zk = new ZooKeeper("localhost:2181", TIME_OUT, new Watcher() {
                public void process(WatchedEvent watchedEvent) {
//                    System.out.println("接受到通知了:类型 "+watchedEvent.getType());
                    log.info("接受到通知了:类型 "+watchedEvent.getType());
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
        return zk;
    }

    public static void main(String[] args) {
         ZooKeeper zk = connect();
         if (zk!=null){
             try {
                 //zk.create("/jasoncc","jasoncc".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
                 log.info("创建持久化节点");
                 Stat stat = zk.exists("/jasoncc",true);
                 long owner = stat.getEphemeralOwner();
                 log.info("该节点持有人:",owner);
             } catch (KeeperException e) {
                 e.printStackTrace();
             } catch (InterruptedException e) {
                 e.printStackTrace();
             }
         }
    }
}
