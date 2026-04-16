package Dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import db.ConnectionDB;
import model.User;

public class UserDAO {

  // acceso de datos para usuarios
  public boolean insertNewUser(User user) {
    String sql = "INSERT INTO users (username, password, email, is_active) VALUES (?, ?, ?, ?)";
    try (PreparedStatement preparedStatement = ConnectionDB.getConnection().prepareStatement(sql)) {
      preparedStatement.setString(1, user.getUsername());
      preparedStatement.setString(2, user.getPassword());
      preparedStatement.setString(3, user.getEmail());
      preparedStatement.setBoolean(4, user.isActive());
      preparedStatement.executeUpdate();
      return true;
    } catch (SQLException e) {
      System.err.println("Error al insertar el usuario: " + e.getMessage());
      return false;
    }
  }

}

  // verifica si ya existe un usuario con ese username en la BD.
public boolean existUsername(String username) {
        String sql = "SELECT COUNT(*) FROM users WHERE username = ?";
        try (PreparedStatement preparedStatement = ConnectionDB.getConnection().prepareStatement(sql)) {
            preparedStatement.setString(1, username);
            ResultSet rs = preparedStatement.executeQuery();
            if (rs.next()) {
                int count = rs.getInt(1);
                return count > 0;
            }
        
            return false;
        } catch (SQLException e) {
            System.err.println("Error verificar username: " + e.getMessage());
            return false;
        }
    }