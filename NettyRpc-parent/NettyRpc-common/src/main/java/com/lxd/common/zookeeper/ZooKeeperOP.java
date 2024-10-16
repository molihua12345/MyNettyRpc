package com.lxd.common.zookeeper;

import org.I0Itec.zkclient.ZkClient;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class ZooKeeperOP {
    private final static String zkAddress = "47.120.60.82:2181";
    private final static ZkClient zkClient = new ZkClient(zkAddress);

    public static void register(String serviceName,String serviceAddress){
        if (!zkClient.exists(serviceName)){
            zkClient.createPersistent(serviceName);
        }
        zkClient.createEphemeral(serviceName+"/"+serviceAddress);
        System.out.printf("create node %s \n",serviceName + "/"+serviceAddress);
    }

    public static String discover(String serviceName){
        List<String> children = zkClient.getChildren(serviceName);
        if (CollectionUtils.isEmpty(children)){
            return "";
        }
        return children.get(ThreadLocalRandom.current().nextInt(children.size()));
    }
}
