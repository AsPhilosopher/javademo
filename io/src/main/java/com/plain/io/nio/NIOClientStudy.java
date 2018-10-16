package com.plain.io.nio;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

public class NIOClientStudy {
    private static Selector selector;
    private static int sendCount = 0;

    public static void init() throws IOException {
        selector = Selector.open();
        SocketChannel sc = SocketChannel.open();
        sc.configureBlocking(false);
        sc.connect(new InetSocketAddress(InetAddress.getLocalHost(), 8099));
        sc.register(selector, SelectionKey.OP_CONNECT);
    }

    public static void listen() throws IOException {
        while (true) {
            selector.select();
            Set<SelectionKey> selectedKeys = selector.selectedKeys();
            Iterator<SelectionKey> it = selectedKeys.iterator();
            while (it.hasNext()) {
                SelectionKey key = it.next();
                it.remove();

                if (key.isConnectable()) {
                    System.out.println("------Connectable-----");
                    SocketChannel channel = (SocketChannel) key.channel();
                    if (channel.finishConnect()) {
                        channel.configureBlocking(false);
                        channel.register(selector, SelectionKey.OP_READ | SelectionKey.OP_WRITE);
                    }
                } else if (key.isReadable()) {
                    System.out.println("-----Readable-----");
                    read(key);
                    // 改变自身关注事件
//                    key.interestOps(SelectionKey.OP_READ | SelectionKey.OP_WRITE);
                } else if (key.isWritable()) {
                    System.out.println("-----Writable-----");
                    write(key);
                    // 改变自身关注事件
//                    key.interestOps(SelectionKey.OP_READ | SelectionKey.OP_WRITE);
                }

            }
        }
    }

    private static void write(SelectionKey key) throws IOException {
        SocketChannel channel = (SocketChannel) key.channel();
        String sendMsg = "client write " + sendCount++;
        System.out.println(sendMsg);
        channel.write(ByteBuffer.wrap(sendMsg.getBytes()));
    }

    private static void read(SelectionKey key) throws IOException {
        SocketChannel channel = (SocketChannel) key.channel();
        ByteBuffer buffer = ByteBuffer.allocate(1024);
        int count = channel.read(buffer);
        byte[] bytes = new byte[count];
        buffer.flip();
        buffer.get(bytes);
        System.out.println(new String(bytes));
    }

    public static void main(String[] args) throws IOException {
        init();
        listen();
    }
}
