package test;

import db.ConexionDb;
import java.sql.*;

public class testDb {
    public static void main(String[] args) {
        try {
            ConexionDb.getConnection();
            Statement statement = ConexionDb.getConnection().createStatement();
            ResultSet rs = statement.executeQuery("SELECT * FROM users");
            while (rs.next()) {
                System.out.println("Usuario: "+ rs.getString("username"));
            }
            System.out.println("Conexión exitosa a la base de datos.");
        } catch (SQLException e) {
            System.out.println("Error al conectar a la base de datos: " + e.getMessage());
        }
    }
}
