package com.plain.io.aio;

import java.io.IOException;

/**
 * Created with IntelliJ IDEA
 * Date: 2018/3/16
 * Time: 下午3:07
 *
 * @author 陈樟杰
 */
public class TimeServer {
    public static void main(String[] args) throws IOException {
        int port = 8080;
        if (args != null && args.length > 0) {
            try {
                port = Integer.valueOf(args[0]);
            } catch (NumberFormatException e) {

            }
        }
        AsyncTimeServerHandler timeServerHandler = new AsyncTimeServerHandler(port);
        new Thread(timeServerHandler, "AIOServer").start();
    }
}
