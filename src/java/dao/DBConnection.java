package dao;

import java.sql.*;

public class DBConnection {
    private static String url = "jdbc:mysql://localhost:3306/ecommerce";
    private static String user = "root";
    private static String pass = "root";

    public static Connection getConnection() {
        Connection con = null;
        try {
            Class.forName("com.mysql.jdbc.Driver");
            con = DriverManager.getConnection(url, user, pass);
        } catch(Exception e){
            e.printStackTrace();
        }
        return con;
    }
}
