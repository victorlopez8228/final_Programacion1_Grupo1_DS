package db;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConexionDb {
    public static void main(String[] args) {
        String url = "jdbc:postgresql://141.148.165.152:22009/user_management_db";
        String username = "postgres";
        String password = "Ubntu-2209**";

        try (Connection connection = DriverManager.getConnection(url, username, password)) {
            System.out.println("Conexión exitosa a la base de datos.");
        } catch (SQLException e) {
            System.out.println("Error al conectar a la base de datos: " + e.getMessage());
        }
    }
}