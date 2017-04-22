package com.jason.zk;

import org.apache.zookeeper.*;

import java.io.IOException;

/**
 * Created by chenchen30 on 2017/4/22.
 */
public class ZKQueue {

    private static ZooKeeper zk;

    private static void connect() throws IOException {
       zk = new ZooKeeper("localhost:2181", 5000, new Watcher() {
           public void process(WatchedEvent watchedEvent) {
               if (watchedEvent.getType()==Event.EventType.NodeCreated &&watchedEvent.getPath().equals("/queue/start")){
                   System.out.println("队列创建已经完成了");
               }
               System.out.println("节点改变---"+watchedEvent.getType());
           }
       });
    }

    private static void init() throws KeeperException, InterruptedException {
        System.out.println("zk分布式队列初始化了");
        if (zk.exists("queue",true) == null){
            zk.create("queue","11".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE,CreateMode.PERSISTENT);
        }else {
            System.out.println("队列已经存在了");
        }
    }
}
