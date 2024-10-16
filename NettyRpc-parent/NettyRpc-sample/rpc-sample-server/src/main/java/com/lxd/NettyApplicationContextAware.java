package com.lxd;

import com.lxd.common.annotation.RpcServer;
import com.lxd.server.NettyServer;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;


@Component
public class NettyApplicationContextAware implements ApplicationContextAware {
    //private static final Logger logger = LoggerFactory.getLogger(NettyApplicationContextAware.class);

    @Value("${zk.address}")
    private String zkAddress;

    @Value("${zk.port}")
    private int zkPort;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        Map<String,Object> rpcBeanMap = new HashMap<>();
        for (Object object:applicationContext.getBeansWithAnnotation(RpcServer.class).values()){
            rpcBeanMap.put("/"+object.getClass().getAnnotation(RpcServer.class).cls().getName(),object);
        }
        try {
            NettyServer.start(zkAddress,zkPort,rpcBeanMap);
        } catch (Exception e) {
            //logger.error("netty server start fail:"+e.getMessage());
        }
    }
}
