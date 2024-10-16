package com.lxd.common.serialize;

import com.alibaba.fastjson.JSON;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

public class RpcEncoder extends MessageToByteEncoder {

    private final Class<?> genericClass;

    public RpcEncoder(Class<?> genericClass){
        this.genericClass = genericClass;
    }

    @Override
    protected void encode(ChannelHandlerContext channelHandlerContext, Object msg, ByteBuf out) throws Exception {
        if (genericClass.isInstance(msg)){
           byte[] data = JSON.toJSONBytes(msg);
           out.writeInt(data.length);
           out.writeBytes(data);
        }
    }
}
