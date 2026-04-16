package db;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConexionDb {
    private static final String url = "jdbc:postgresql://141.148.165.152:22009/user_management_db";
    private static final String username = "postgres";
    private static final String password = "Ubntu-2209**";   

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(url, username, password);
    }

}