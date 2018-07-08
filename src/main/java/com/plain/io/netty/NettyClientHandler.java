package com.plain.io.netty;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;

/**
 * Created with IntelliJ IDEA
 * Date: 2018/3/23
 * Time: 下午5:46
 *
 * @author 陈樟杰
 */
public class NettyClientHandler extends ChannelHandlerAdapter {
    private final ByteBuf message;

    public NettyClientHandler() {
        byte[] req = "time".getBytes();
        message = Unpooled.buffer(req.length);
        message.writeBytes(req);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {

        ctx.close();
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {

        ctx.writeAndFlush(message);
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {

        ByteBuf buf = (ByteBuf) msg;
        byte[] req = new byte[buf.readableBytes()];
        buf.readBytes(req);
        String body = new String(req, "UTF-8");
        System.out.println("this client receiver data is:" + body);
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {

    }
}
