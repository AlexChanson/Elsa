package dao;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class MYSQL {
    private static Connection connection;
    private static String url = "jdbc:mysql://localhost:3306", user = "root", password = "root";

    static {
        init();
    }


    private static void init() {
        InputStream input;
        input = MYSQL.class.getClassLoader().getResourceAsStream("sql.properties");
        if (input != null) {
            try {
                Properties properties = new Properties();
                properties.load(input);
                url = properties.getProperty("url");
                user = properties.getProperty("user");
                password = properties.getProperty("password");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        try {
            Class.forName("com.mysql.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        try {
            connection = DriverManager.getConnection(url, user, password);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static Connection getConnection() {
        try {
            if (connection.isClosed()) {
                connection = null;
                init();
                return connection;
            }
        } catch (SQLException e) {
            connection = null;
            init();
            return connection;
        }
        return connection;

    }
}
