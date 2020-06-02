package com.plain.springboot.web;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class HttpTest {
    public static void main(String[] args) {
        AtomicInteger atomicInteger = new AtomicInteger();
        List<Thread> runnableList = new ArrayList<>();
        for (int i = 0; i < 1; i++) {
            runnableList.add(new Thread(() -> {
                request(atomicInteger.addAndGet(1));
//                httpClientRequest(atomicInteger.addAndGet(1));
            }, "MyHttp-" + i));
        }
        runnableList.forEach(item -> {
            item.start();
            /*try {
                TimeUnit.MILLISECONDS.sleep(300);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }*/
        });
    }


    private static void request(int no) {
        // 打开和URL之间的连接
        try {
            URL realUrl = new URL("http://localhost:8088/haha");
            URLConnection connection = realUrl.openConnection();
            // 设置通用的请求属性
            connection.setRequestProperty("accept", "*/*");
//            connection.setRequestProperty("connection", "Keep-Alive");
            connection.setRequestProperty("user-agent",
                    "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
            // 建立实际的连接
            System.out.println("#####: " + no);

            long start = System.currentTimeMillis();
            connection.connect();
            System.out.println("@@@@@: " + no);
            // 获取所有响应头字段
//                Map<String, List<String>> map = connection.getHeaderFields();

            byte[] bytes = new byte[256];
            connection.getInputStream().read(bytes);
            String rsp = new String(bytes);
            long end = System.currentTimeMillis();
//                System.out.println("Response: " + no + " " + (end - start) + ": " + map);
            System.out.println("Response: " + no + " " + (end - start) + ": " + rsp);
        } catch (IOException e) {
            System.out.println("@@@@@$$$$$$: " + no + ": " + e.getMessage());
        }
    }

    private static void httpClientRequest(int no) {
        //1.打开浏览器
        CloseableHttpClient httpClient = HttpClients.createDefault();
        //2.声明get请求
        HttpGet httpGet = new HttpGet("http://localhost:8088/haha");
        //3.发送请求
        try {
            System.out.println("#####: " + no);

            CloseableHttpResponse response = httpClient.execute(httpGet);
            System.out.println("@@@@@: " + no);
            System.out.println("Response: " + no + ": " + response);
        } catch (IOException e) {
            System.out.println("@@@@@$$$$$$: " + no + ": " + e.getMessage());
        }
    }
}
