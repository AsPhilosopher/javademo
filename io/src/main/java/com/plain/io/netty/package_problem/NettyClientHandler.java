package com.plain.io.netty.package_problem;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;

/**
 * Created with IntelliJ IDEA
 * Date: 2018/3/24
 * Time: 下午2:33
 *
 * @author 陈樟杰
 */
public class NettyClientHandler extends ChannelHandlerAdapter {
    private int counter;

    private byte[] req;

    public NettyClientHandler() {
        req = ("time" + System.getProperty("line.separator")).getBytes();

    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        ctx.close();

    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        ByteBuf message = null;
        for (int i = 0; i < 100; i++) {
            message = Unpooled.buffer(req.length);
            message.writeBytes(req);
            ctx.writeAndFlush(message);
        }
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {

       /* ByteBuf buf = (ByteBuf) msg;
        byte[] req = new byte[buf.readableBytes()];
        buf.readBytes(req);
        String body = new String(req, "UTF-8");
        System.out.println("this client receiver data is:" + body + "; this is counter is " + ++counter);*/

        String body=(String)msg;
        System.out.println("this client receiver data is:"+body+"; this is counter is"+ ++counter);
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {

    }
}
