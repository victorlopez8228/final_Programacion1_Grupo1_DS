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

  //lista de todos los usuarios
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
                    rs.getBoolean("is_active")
                ));
            }
        } catch (SQLException e) {
            System.err.println("Error listar usuarios: " + e.getMessage());
        }
        return lista;
    }