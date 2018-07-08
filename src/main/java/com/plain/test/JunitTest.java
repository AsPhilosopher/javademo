package com.plain.test;

import org.junit.Test;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class JunitTest {
    @Test
    public void test() {
        System.out.println("Hello");

        System.out.println(getV4IP());
    }

    private static String getV4IP() {
        String ip = "";
        String chinaz = "http://ip.chinaz.com/";

        String inputLine = "";
        String read = "";
        try {
            URL url = new URL(chinaz);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            BufferedReader in = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
            while ((read = in.readLine()) != null) {
                inputLine += read;
            }
            System.out.println(inputLine);

        } catch (Exception e) {
            e.printStackTrace();
        }

        Pattern p = Pattern.compile("\\<strong class\\=\"red\">(.*?)\\<\\/strong>");
        Matcher m = p.matcher(inputLine);
        if (m.find()) {
            String ipstr = m.group(1);
            System.out.println(ipstr);
        }
        return ip;
    }
}
