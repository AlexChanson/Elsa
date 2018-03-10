package dao;

import server.Server;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

/**
 * A simple Singleton to hold our connection to the database
 */
public class MYSQL {
    private static Connection connection = null;
    private static String url = "jdbc:mysql://localhost:3306", user = "root", password = "root";

    /**
     * This methods instantiates the Singleton
     */
    private static void init() {
        // We try to extract the parameters from the config files
        // Only works if app is packaged in a jar file
        InputStream input;
        input = MYSQL.class.getClassLoader().getResourceAsStream("config.properties");
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

        // We recover the command line arguments if any
        if (Server.jdbc != null)
            url = Server.jdbc;
        if (Server.dbuser != null)
            user = Server.dbuser;
        if (Server.dbpass != null)
            password = Server.dbpass;

        try {
            connection = DriverManager.getConnection(url, user, password);
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public static Connection getConnection() {
        try {
            if (connection == null || connection.isClosed()) {
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
