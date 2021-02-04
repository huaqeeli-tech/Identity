//package com.huaqeeli.training;
//
//
//import java.io.IOException;
//import java.sql.Connection;
//import java.sql.DriverManager;
//
//public class DBController {
//
//      private static final String DATABASE_URL = "jdbc:mysql://localhost:3306/trainingdb?useSSL=false";
//    private static final String DATABASE_USERNAME = "root";
//    private static final String DATABASE_PASSWORD = "root";
//    private static Connection con;
//
//    public static Connection dbConnector() throws IOException {
//
//       Connection connection = DriverManager.getConnection(DATABASE_URL, DATABASE_USERNAME, DATABASE_PASSWORD);
//        return con;
//    }
//
//}
