package com.plain.io.netty.package_problem;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;

/**
 * Created with IntelliJ IDEA
 * Date: 2018/3/24
 * Time: 下午2:31
 *
 * @author 陈樟杰
 */
public class NettyServerHandler extends ChannelHandlerAdapter {
    private int counter;

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        ctx.close();
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        /*ByteBuf buf = (ByteBuf) msg;
        byte[] req = new byte[buf.readableBytes()];
        buf.readBytes(req);
        String body = new String(req, "UTF-8").substring(0, req.length - System.getProperty("line.separator").length());
        System.out.println("the server receiver data is:" + body + "; the counter is:" + ++counter);

        String currentTime = "time".equalsIgnoreCase(body) ? new java.util.Date(System.currentTimeMillis()).toString() : "no zuo no die";
        currentTime = currentTime + System.getProperty("line.separator");
        ByteBuf resp = Unpooled.copiedBuffer(currentTime.getBytes());
        ctx.writeAndFlush(resp);*/


        String body=(String)msg;
        System.out.println("the server receiver data is:"+body+"; the counter is:"+ ++counter);
        String currentTime="time".equalsIgnoreCase(body) ? new java.util.Date(System.currentTimeMillis()).toString():"no zuo no die";
        currentTime=currentTime+ System.getProperty("line.separator");
        ByteBuf resp= Unpooled.copiedBuffer(currentTime.getBytes());
        ctx.writeAndFlush(resp);
    }
}
