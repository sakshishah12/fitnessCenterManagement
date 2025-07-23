package ise503_final;

import java.sql.Connection;
import java.sql.DriverManager;

public class DBUtil {
    public static Connection getConnection() throws Exception {
        Class.forName("com.mysql.cj.jdbc.Driver");
        return DriverManager.getConnection(
            "jdbc:mysql://localhost:3306/ConanFitness_db?useSSL=false&serverTimezone=UTC",
            "root",
            "root"
        );
    }
}
