package Dao;

import java.sql.PreparedStatement;
import java.sql.SQLException;

import model.User;

public class UserDAO {

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