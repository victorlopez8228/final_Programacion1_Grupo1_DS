package Dao;

import java.sql.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import db.ConexionDb;


public class UserDAO {
    public void registerUser(String username, String password, String email) {
         String sqlCheck = "SELECT 1 FROM users WHERE username = ?";
         String sqlInsert = "INSERT INTO users (username, password, email, is_active) VALUES (?, ?, ?, TRUE)";

         try (Connection con = ConexionDb.getConnection()) {

            PreparedStatement psCheck = con.prepareStatement(sqlCheck);
            psCheck.setString(1, username);
            if (psCheck.executeQuery().next()) {
                System.out.println("Error: EL nombre de usuario'" + username + "'ya existe");
                return;
            }
            PreparedStatement  psInsert = con.prepareStatement(sqlInsert);
            psInsert.setString(1, username);
            psInsert.setString(2, password);
            psInsert.setString(3, email);
            psInsert.executeUpdate();
            System.out.println("Usuario registrado exitosamente");
            
         }catch (SQLException e) {
            System.out.println("Error al verificar el nombre de usuario: " + e.getMessage());
            return;
          }
    }
}