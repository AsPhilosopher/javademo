package com.plain.io.netty;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

/**
 * Created with IntelliJ IDEA
 * Date: 2018/3/23
 * Time: 下午5:45
 *
 * @author 陈樟杰
 */
public class NettyClient {
    public static void main(String[] args) throws Exception {
        int port = 17666;
        new NettyClient().connect(port, "127.0.0.1");

    }

    public void connect(int port, String host) throws Exception {
        //配置客户端NIO线程池
        EventLoopGroup workGroup = new NioEventLoopGroup();
        try {
            Bootstrap b = new Bootstrap();
            /**
             * TCP_NODELAY：是否启用心跳保活机制。在双方TCP套接字建立连接后（即都进入ESTABLISHED状态）并且在两个小时左右上层没有任何数据传输的情况下，这套机制才会被激活。
             */
            b.group(workGroup).channel(NioSocketChannel.class).option(ChannelOption.TCP_NODELAY, true).handler(new ChannelInitializer<SocketChannel>() {
                @Override
                protected void initChannel(SocketChannel socketChannel) throws Exception {
                    /**
                     * 作用是当创建NioSocketChannel成功之后，在初始化它的时候将他的ChannelHandler设置到ChannelPipeline中，用于处理IO事件
                     */
                    socketChannel.pipeline().addLast(new NettyClientHandler());
                }
            });
            //发起异步连接操作
            ChannelFuture f = b.connect(host, port).sync();
            //等待客户端链路关闭
            f.channel().closeFuture().sync();

        } finally {
            //释放NIO 线程组
            workGroup.shutdownGracefully();

        }
    }
}
