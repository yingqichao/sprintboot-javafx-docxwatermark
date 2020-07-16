package com.google.test;

import java.sql.*;

public class Tt {
    public static void main(String[] args) throws Exception {
        //加载数据库驱动程序
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException cne) {
            cne.printStackTrace();
        }
        String url = "jdbc:mysql://127.0.0.1:3306";
        String username = "root";
        String password = "123456";
        String sql = "******";
        Connection conn = null;
        Statement stmt = null;
        try {
            conn = DriverManager.getConnection(url, username, password);
            stmt = conn.createStatement();
            ResultSet rst = stmt.executeQuery(sql);
            stmt.close();
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (stmt != null) {
                stmt.close();
            }
            if (conn != null) {
                conn.close();
            }
        }


    }
}