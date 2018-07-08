package com.plain.io.netty;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

/**
 * Created with IntelliJ IDEA
 * Date: 2018/3/23
 * Time: 下午5:41
 *
 * @author 陈樟杰
 */
public class NettyServer {
    public static void main(String[] args) throws Exception {
        int port = 17666;
        new NettyServer().bind(port);
    }

    public void bind(int port) throws Exception {
        /**
         * 配置服务端的NIO线程池
         * 一个用于服务端接收客户端连接 另一个用于进行SocketChannel的网络读写
         */
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workGroup = new NioEventLoopGroup();
        try {
            ServerBootstrap b = new ServerBootstrap();
            /**
             * .channel(NioServerSocketChannel.class) 设置Channel为NioServerSocketChannel
             * .childHandler(new NettyServerHandler()) 绑定IO事件的处理类
             * BACKLOG用于构造服务端套接字ServerSocket对象，标识当服务器请求处理线程全满时，用于临时存放已完成三次握手的请求的队列的最大长度。如果未设置或所设置的值小于1，Java将使用默认值50
             */
            b.group(bossGroup, workGroup).channel(NioServerSocketChannel.class).option(ChannelOption.SO_BACKLOG, 1024).childHandler(new NettyServerHandler());
            //绑定端口，等待同步成功
            ChannelFuture f = b.bind(port).sync();
            //等待服务端关闭监听端口 该方法阻塞 等待服务端链路关闭之后才退出main函数
            f.channel().closeFuture().sync();

        } finally {
            //释放线程池资源
            bossGroup.shutdownGracefully();
            workGroup.shutdownGracefully();
        }
    }

    //添加内部类
    private class ChildChannelHandler extends ChannelInitializer<SocketChannel> {
        @Override
        protected void initChannel(SocketChannel socketChannel) throws Exception {
            socketChannel.pipeline().addLast(new NettyServerHandler());
        }
    }
}
