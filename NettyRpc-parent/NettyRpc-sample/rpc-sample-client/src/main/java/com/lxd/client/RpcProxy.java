package com.lxd.client;

import com.lxd.common.http.Request;
import com.lxd.common.http.Response;
import org.springframework.cglib.proxy.Proxy;


import java.util.UUID;


public class RpcProxy {
    public static  <T> T create(final Class<?>cls){
        return (T) Proxy.newProxyInstance(cls.getClassLoader(),new Class<?>[]{cls},(o, method, objects)->{

            Request request = new Request();
            request.setInterfaceName("/"+cls.getName());
            request.setRequestId(UUID.randomUUID().toString());
            request.setParameter(objects);
            request.setMethodName(method.getName());
            request.setParameterTypes(method.getParameterTypes());

            Response response = new NettyClient().client(request);
            return response.getResult();
        });
    }
}
