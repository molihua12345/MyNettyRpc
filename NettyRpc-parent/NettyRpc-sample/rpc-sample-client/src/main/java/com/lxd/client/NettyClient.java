package com.lxd.client;

import com.lxd.common.http.Request;
import com.lxd.common.http.Response;
import com.lxd.common.serialize.RpcDecoder;
import com.lxd.common.serialize.RpcEncoder;
import com.lxd.common.zookeeper.ZooKeeperOP;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

public class NettyClient extends SimpleChannelInboundHandler<Response> {

    private Response response;

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        ctx.close();
    }


    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, Response response) throws Exception {
        this.response = response;
    }

    public Response client(Request request)throws Exception{
        EventLoopGroup group = new NioEventLoopGroup();
        try {
            Bootstrap bootstrap = new Bootstrap();
            bootstrap.group(group)
                    .channel(NioSocketChannel.class)
                    .option(ChannelOption.TCP_NODELAY,true)
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel socketChannel) throws Exception {
                               socketChannel.pipeline()
                                       .addLast(new RpcDecoder(Response.class))
                                       .addLast(new RpcEncoder(Request.class))
                                       .addLast(NettyClient.this);
                        }
                    });
            String[] discover = ZooKeeperOP.discover(request.getInterfaceName()).split(":");
            ChannelFuture future = bootstrap.connect(discover[0], Integer.parseInt(discover[1])).sync();
            Channel channel = future.channel();
            channel.writeAndFlush(request).sync();
            channel.closeFuture().sync();

            return response;
        }finally {
            group.shutdownGracefully();
        }
    }
}
