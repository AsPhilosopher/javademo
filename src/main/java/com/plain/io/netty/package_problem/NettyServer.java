package com.plain.io.netty.package_problem;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.LineBasedFrameDecoder;
import io.netty.handler.codec.string.StringDecoder;

/**
 * Created with IntelliJ IDEA
 * Date: 2018/3/24
 * Time: 下午2:29
 *
 * @author 陈樟杰
 */
public class NettyServer {
    public static void main(String[] args) throws Exception {
        int port = 17666;
        new NettyServer().bind(port);


    }

    public void bind(int port) throws Exception {
        //配置服务端的NIO线程池
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workGroup = new NioEventLoopGroup();
        try {
            ServerBootstrap b = new ServerBootstrap();
            /**
             * .childHandler(用内部类 则是解决粘包问题的方案)
             */
            b.group(bossGroup, workGroup).channel(NioServerSocketChannel.class).option(ChannelOption.SO_BACKLOG, 1024).childHandler(new ChildChannelHandler());
            //绑定端口，等待同步成功
            ChannelFuture f = b.bind(port).sync();
            //等待服务端关闭监听端口
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
            socketChannel.pipeline().addLast(new LineBasedFrameDecoder(1024));
            socketChannel.pipeline().addLast(new StringDecoder());
            socketChannel.pipeline().addLast(new NettyServerHandler());

        }
    }
}
