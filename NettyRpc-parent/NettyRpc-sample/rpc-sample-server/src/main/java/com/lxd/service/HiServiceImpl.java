package com.lxd.service;

import com.lxd.common.annotation.RpcServer;

@RpcServer(cls = HiService.class)
public class HiServiceImpl implements HiService{
    @Override
    public String hi(String msg) {
        return msg;
    }
}
