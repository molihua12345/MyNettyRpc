package com.lxd.service;

import com.lxd.common.annotation.RpcServer;

@RpcServer(cls=HelloService.class)
public class HelloServiceImpl implements HelloService{
    @Override
    public String hello(String msg) {
        return msg;
    }
}
