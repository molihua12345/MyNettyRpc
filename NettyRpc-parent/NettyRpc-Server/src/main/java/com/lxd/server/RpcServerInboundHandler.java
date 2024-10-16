package com.lxd.server;


import com.lxd.common.http.Request;
import com.lxd.common.http.Response;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;


import java.lang.reflect.Method;
import java.util.Map;

public class RpcServerInboundHandler extends ChannelInboundHandlerAdapter {
    //private static final Logger logger = LoggerFactory.getLogger(RpcServerInboundHandler.class);

    private final Map<String,Object> handle;


    public RpcServerInboundHandler(Map<String, Object> handle) {
        this.handle = handle;
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx,Object msg) throws Exception {
        Request request = (Request) msg;
        //logger.info("request data {}", JSON.toJSONString(request));
        //jdk reflect method invoke
        Object bean = handle.get(request.getInterfaceName());
        Method method = bean.getClass().getMethod(request.getMethodName(),request.getParameterTypes());
        method.setAccessible(true);
        Object result = method.invoke(bean, request.getParameter());

        Response response = new Response();
        response.setRequestId(request.getRequestId());
        response.setResult(result);
        // close the connection actively after sending response
        ctx.writeAndFlush(response).addListener(ChannelFutureListener.CLOSE);
    }
    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) {
        ctx.flush();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        ctx.close();
    }
}
