package Dao;

impport java.sql.*;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

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

  // lista de todos los usuarios
  public list<User> listallUsers() {
    List<User> lista = new ArrayList<>();
    String sql = "SELECT id, username, password, email, is_active FROM users ORDER BY id";
    try (Statement prepStatement = ConnectionDB.getConnection().createStatement();
        ResultSet rs = prepStatement.executeQuery(sql)) {
      while (rs.next()) {
        lista.add(new User(
            rs.getInt("id"),
            rs.getString("username"),
            rs.getString("password"),
            rs.getString("email"),
            rs.getBoolean("is_active")));
      }
    } catch (SQLException e) {
      System.err.println("Error listar usuarios: " + e.getMessage());
    }
    return lista;
  }

  // busqueda de usuarios por username
  public User searchByUsername(String username) {
    String sql = "SELECT id, username, password, email, is_active FROM users WHERE username = ?";
    try (PreparedStatement preparedStatement = ConnectionDB.getConnection().prepareStatement(sql)) {
      preparedStatement.setString(1, username);
      ResultSet rs = preparedStatement.executeQuery();
      if (rs.next()) {
        return new User(
            rs.getInt("id"),
            rs.getString("username"),
            rs.getString("password"),
            rs.getString("email"),
            rs.getBoolean("is_active"));
      }
    } catch (SQLException e) {
      System.err.println("Error buscar usuario: " + e.getMessage());
    }
    return null;
  }

  // busqueda de usuarios por id
  public User searchById(int id) {
    String sql = "SELECT id, username, password, email, is_active FROM users WHERE id = ?";
    try (PreparedStatement preparedStatement = ConnectionDB.getConnection().prepareStatement(sql)) {
      preparedStatement.setInt(1, id);
      ResultSet rs = preparedStatement.executeQuery();
      if (rs.next()) {
        return new User(
            rs.getInt("id"),
            rs.getString("username"),
            rs.getString("password"),
            rs.getString("email"),
            rs.getBoolean("is_active"));
      }
    } catch (SQLException e) {
      System.err.println("Error buscar por id: " + e.getMessage());
    }
    return null;
  }

  // actualizacion del campo username indicado por id
  public boolean updateUsername(int id, String nuevoUsername) {
    String sql = "UPDATE users SET username = ? WHERE id = ?";
    try (PreparedStatement preparedStatement = ConnectionDB.getConnection().prepareStatement(sql)) {
      preparedStatement.setString(1, nuevoUsername);
      preparedStatement.setInt(2, id);
      return preparedStatement.executeUpdate() > 0;
    } catch (SQLException e) {
      System.err.println("Error actualizar username: " + e.getMessage());
      return false;
    }
  }

  // actualizacion del campo password (hash SHA-256) indicado por id
  public boolean updatePassword(int id, String nuevaPasswordHash) {
    String sql = "UPDATE users SET password = ? WHERE id = ?";
    try (PreparedStatement preparedStatement = ConnectionDB.getConnection().prepareStatement(sql)) {
      preparedStatement.setString(1, nuevaPasswordHash);
      preparedStatement.setInt(2, id);
      return preparedStatement.executeUpdate() > 0;
    } catch (SQLException e) {
      System.err.println("Error actualizar password: " + e.getMessage());
      return false;
    }
  }

  // activacion o desactivacion de usuario cambiondo el campo is_active
  public boolean changeStatus(int id, boolean nuevoEstado) {
    String sql = "UPDATE users SET is_active = ? WHERE id = ?";
    try (PreparedStatement preparedStatement = ConnectionDB.getConnection().prepareStatement(sql)) {
      preparedStatement.setBoolean(1, nuevoEstado);
      preparedStatement.setInt(2, id);
      return preparedStatement.executeUpdate() > 0;
    } catch (SQLException e) {
      System.err.println("Error cambiar estado: " + e.getMessage());
      return false;
    }
  }

  // eliminacion de usuario permanentemente por id
  public boolean deleteUser(int id) {
    String sql = "DELETE FROM users WHERE id = ?";
    try (PreparedStatement preparedStatement = ConnectionDB.getConnection().prepareStatement(sql)) {
      preparedStatement.setInt(1, id);
      return preparedStatement.executeUpdate() > 0;
    } catch (SQLException e) {
      System.err.println("Error eliminar usuario: " + e.getMessage());
      return false;
    }
  }
}
