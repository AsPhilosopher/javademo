package com.plain.io.nio;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.ForkJoinPool;

public class MyNIOServer {
    private final ForkJoinPool forkjoinPool = new ForkJoinPool((Runtime.getRuntime().availableProcessors() << 1));

    private Selector selector;

    public void start(int port) {
        try {
            init(port);
            dispatch();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void init(int port) throws IOException {
        selector = Selector.open();
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
        serverSocketChannel.configureBlocking(false);
        serverSocketChannel.bind(new InetSocketAddress(InetAddress.getLocalHost(), port));
        serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
    }

    private void dispatch() throws IOException {
        while (true) {
            //该方法会阻塞
            selector.select();
            Set<SelectionKey> selectedKeys = selector.selectedKeys();
            Iterator<SelectionKey> it = selectedKeys.iterator();
            while (it.hasNext()) {
                SelectionKey key = it.next();
                // 由于select操作只管对selectedKeys进行添加，所以key处理后我们需要从里面把key去掉
                it.remove();
                if (key.isAcceptable()) {
                    ServerSocketChannel serverChannel = (ServerSocketChannel) key.channel();
                    // 得到与客户端的套接字通道
                    SocketChannel channel = serverChannel.accept();
                    channel.configureBlocking(false);
                    // 同样将于客户端的通道在selector上注册，,可以通过key获取关联的选择器
                    channel.register(selector, SelectionKey.OP_READ);
                } else if (key.isReadable()) {
                    forkjoinPool.execute(() -> {
                        try {
                            handle(key);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    });
                }
            }
        }
    }

    private void handle(SelectionKey key) throws IOException {
        SocketChannel channel = (SocketChannel) key.channel();
        if (!channel.isConnected() || !channel.isOpen()) {
            return;
        }
        ByteBuffer buffer = ByteBuffer.allocate(1024);
        int count = channel.read(buffer);
        if (0 == count) {
            return;
        }
        if (-1 == count) {
            channel.close();
            key.channel();
            return;
        }
        byte[] bytes = new byte[count];
        buffer.flip();
        buffer.get(bytes);
        String arg = new String(bytes);
        System.out.println("Arg: " + arg);
        String result = "SERVER DONE:" + arg;
        channel.write(ByteBuffer.wrap(result.getBytes()));
    }

    public static void main(String[] args) {
        MyNIOServer myNIOServer = new MyNIOServer();
        myNIOServer.start(8099);
    }
}
