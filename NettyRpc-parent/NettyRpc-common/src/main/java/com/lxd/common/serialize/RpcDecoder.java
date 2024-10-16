package com.lxd.common.serialize;

import com.alibaba.fastjson.JSON;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageDecoder;

import java.util.List;

public class RpcDecoder extends MessageToMessageDecoder<ByteBuf> {
    private Class<?> genericClass;

    public RpcDecoder(Class<?> genericClass){
        this.genericClass = genericClass;
    }

    @Override
    protected void decode(ChannelHandlerContext channelHandlerContext, ByteBuf msg, List<Object> out) throws Exception {
        if (msg.readableBytes() < 4) {
            return;
        }
        msg.markReaderIndex();
        int dataLength = msg.readInt();
        if (msg.readableBytes() < dataLength) {
            msg.resetReaderIndex();
            return;
        }
        byte[] data = new byte[dataLength];
        msg.readBytes(data);

        out.add(JSON.parseObject(data, genericClass));
    }
}
