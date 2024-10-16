package com.lxd.client;

import com.lxd.service.HelloService;
import com.lxd.service.HiService;

public class RpcClientApplication {
    public static void main(String[] args) {
        HiService hiService = RpcProxy.create(HiService.class);
        System.out.println(hiService.hi("test hi service"));

        HelloService helloService = RpcProxy.create(HelloService.class);
        System.out.println(helloService.hello("test hello service"));
    }
}
