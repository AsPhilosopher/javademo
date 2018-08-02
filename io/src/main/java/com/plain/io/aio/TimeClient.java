package com.plain.io.aio;

/**
 * Created with IntelliJ IDEA
 * Date: 2018/3/16
 * Time: 下午3:11
 *
 * @author 陈樟杰
 */
public class TimeClient {
    public static void main(String[] args) {
        int port = 8080;
        if (args != null && args.length > 0) {
            try {
                port = Integer.valueOf(args[0]);
            } catch (NumberFormatException e) {

            }
        }
        new Thread(new AsyncTimeClientHandler("127.0.0.1", port), "AIOClient").start();
    }
}
