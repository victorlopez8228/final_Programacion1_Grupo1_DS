package test;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import Dao.UserDAO;
import db.ConexionDb;

public class testDb {
    public UserDAO userDAO = new UserDAO();

    public static void main(String[] args) {
        try {
            ConexionDb.getConnection();
            Statement statement = ConexionDb.getConnection().createStatement();
            ResultSet rs = statement.executeQuery("SELECT * FROM users");
            while (rs.next()) {
                System.out.println("Usuario: " + rs.getString("username"));
            }
            System.out.println("Conexión exitosa a la base de datos.");
        } catch (SQLException e) {
            System.out.println("Error al conectar a la base de datos: " + e.getMessage());
        }
        boolean isRegistered = userDAO.registerUser("testuser", "password123", "testuser@example.com");
        System.out.println("Usuario registrado: " + isRegistered);
    }
}
