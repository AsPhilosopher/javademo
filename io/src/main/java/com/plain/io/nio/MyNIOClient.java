package com.plain.io.nio;

import lombok.Data;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

@Data
public class MyNIOClient {
    private Selector selector;
    private SocketChannel socketChannel;
    private int port;
    private String name;
    private int requestCount = 0;

    public void start(int port, String name) {
        try {
            this.port = port;
            this.name = name;
            init();
            request();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void init() throws IOException {
        selector = Selector.open();
        socketChannel = SocketChannel.open();
        socketChannel.configureBlocking(false);
        socketChannel.connect(new InetSocketAddress(InetAddress.getLocalHost(), port));
        socketChannel.register(selector, SelectionKey.OP_CONNECT);
    }

    private void request() throws IOException {
        for (int i = 0; i < 10000; i++) {
            selector.select();
            Set<SelectionKey> selectedKeys = selector.selectedKeys();
            Iterator<SelectionKey> it = selectedKeys.iterator();
            while (it.hasNext()) {
                SelectionKey key = it.next();
                it.remove();

                if (key.isConnectable()) {
                    SocketChannel channel = (SocketChannel) key.channel();
                    if (channel.finishConnect()) {
                        channel.configureBlocking(false);
                        channel.register(selector, SelectionKey.OP_READ);
                        channel.write(ByteBuffer.wrap((name + " args" + ++requestCount).getBytes()));
                    }
                } else if (key.isReadable()) {
                    SocketChannel channel = handleResponse(key);
                    channel.write(ByteBuffer.wrap((name + " args" + ++requestCount).getBytes()));
                }
            }
        }

        selector.select();
        Set<SelectionKey> selectedKeys = selector.selectedKeys();
        Iterator<SelectionKey> it = selectedKeys.iterator();
        while (it.hasNext()) {
            SelectionKey key = it.next();
            it.remove();
            if (key.isReadable()) {
                handleResponse(key);
                System.out.println("FINISH LAST");
            }
        }
//        socketChannel.shutdownInput();
//        socketChannel.shutdownOutput();
        socketChannel.close();
    }

    private SocketChannel handleResponse(SelectionKey key) throws IOException {
        SocketChannel channel = (SocketChannel) key.channel();
        ByteBuffer buffer = ByteBuffer.allocate(1024);
        int count = channel.read(buffer);
        byte[] bytes = new byte[count];
        buffer.flip();
        buffer.get(bytes);
        String result = new String(bytes);
        System.out.println("Result:" + result);
        return channel;
    }

    public static void main(String[] args) throws InterruptedException {
        ForkJoinPool forkjoinPool = new ForkJoinPool((Runtime.getRuntime().availableProcessors() << 1));
        AtomicInteger atomicInteger = new AtomicInteger(0);
        for (int i = 0; i < 2; i++) {
            forkjoinPool.execute(() -> {
                MyNIOClient myNIOClient = new MyNIOClient();
                myNIOClient.start(8099, "client" + atomicInteger.addAndGet(1));
                System.out.println(myNIOClient.getRequestCount());
            });
        }
        int count = 0;
        while (count < 5 && !forkjoinPool.isTerminated()) {
            TimeUnit.SECONDS.sleep(1);
            ++count;
        }
    }
}
