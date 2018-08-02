package com.plain.java.demo;

import java.sql.*;

public class JDBCDemo {
    public static void main(String[] args) throws ClassNotFoundException, SQLException {
        String url = "jdbc:mysql://127.0.0.1:3306/test?useUnicode=true&characterEncoding=utf8";
        String user = "root";
        String password = "asdasdasd";
        String jdbc = "com.mysql.jdbc.Driver";

        //指定连接类型
        Class.forName(jdbc);
        //获取连接
        Connection conn = DriverManager.getConnection(url, user, password);
        PreparedStatement pst = conn.prepareStatement("SELECT * FROM name");
        ResultSet set = pst.executeQuery();
        System.out.println(set.next());
        System.out.println(set.getObject(1));
        System.out.println(set.getObject(2));
    }
}
