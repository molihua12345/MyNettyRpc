package com.lxd.server;

import com.lxd.common.http.Request;
import com.lxd.common.http.Response;
import com.lxd.common.serialize.RpcDecoder;
import com.lxd.common.serialize.RpcEncoder;
import com.lxd.common.zookeeper.ZooKeeperOP;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

import java.util.Map;

public class NettyServer {
   // private static final Logger logger = LoggerFactory.getLogger(NettyServer.class);

    public static void start(String ip , int port, Map<String,Object>params)throws Exception{
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workerGroup = new NioEventLoopGroup();

        try{
            ServerBootstrap serverBootstrap = new ServerBootstrap();
            serverBootstrap.group(bossGroup,workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .option(ChannelOption.SO_BACKLOG,1024)
                    .option(ChannelOption.SO_KEEPALIVE,true)
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel socketChannel) throws Exception {
                            socketChannel.pipeline()
                                    .addLast(new RpcDecoder(Request.class))
                                    .addLast(new RpcEncoder(Response.class))
                                    .addLast(new RpcServerInboundHandler(params));
                        }
                    });
            ChannelFuture future = serverBootstrap.bind(ip,port).sync();
            System.out.println(future.isSuccess());
            if (future.isSuccess()){
                params.keySet().forEach(key -> ZooKeeperOP.register(key,ip+":"+port));
            }
            future.channel().closeFuture().sync();
        }finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }
}
