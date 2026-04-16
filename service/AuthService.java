package service;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;

import Dao.UserDAO;
import model.model.User;

public class AuthService {

  // Capa de Servicio
  private final UserDAO DAO = new UserDAO();

  // SEGURIDAD: HASH SHA-256
  public String hashPassword(String password) {
    try {
      // Obtener instancia del algoritmo SHA-256
      MessageDigest digest = MessageDigest.getInstance("SHA-256");

      // Convertir la contraseña a bytes y aplicar el hash
      byte[] hashBytes = digest.digest(password.getBytes());

      // Convertir el array de bytes a String hexadecimal legible
      StringBuilder hexString = new StringBuilder();
      for (byte b : hashBytes) {
        // Integer.toHexString convierte cada byte a 2 dígitos hex
        String hex = Integer.toHexString(0xff & b);
        if (hex.length() == 1)
          hexString.append('0'); // padding si es 1 dígito
        hexString.append(hex);
      }
      return hexString.toString(); // Ej: "a3f9b2c1e4..." (64 chars)

    } catch (NoSuchAlgorithmException e) {
      System.err.println("SHA-256 no disponible: " + e.getMessage());
      return null;
    }
  }

  // AUTENTICACIÓN
  public User login(String username, String password) {
    if (username == null || username.trim().isEmpty()) {
      return null;
    }
    if (password == null || password.trim().isEmpty()) {
      return null;
    }

    User user = Dao.searchByUsername(username.trim());

    if (user == null) {
      return null; // Usuario no existe
    }
    if (!user.isActive()) {
      return null; // Inactivo no puede entrar
    }

    String hashIngresado = hashPassword(password);
    if (hashIngresado == null) {
      return null;
    }

    // Comparar hash almacenado vs hash del intento actual
    return hashIngresado.equals(user.getPassword()) ? user : null;
  }

  // GESTIÓN DE USUARIOS
  public String registerUser(String username, String password, String email) {
    // Validaciones de campos vacíos
    if (username == null || username.trim().isEmpty()) {
      return "ERROR:El nombre de usuario no puede estar vacío.";
    }

    if (password == null || password.trim().isEmpty()) {
      return "ERROR:La contraseña no puede estar vacía.";
    }
    if (email == null || email.trim().isEmpty()) {
      return "ERROR:El email no puede estar vacío.";
    }

    // Validación de longitud de contraseña
    if (password.length() < 6) {
      return "ERROR:La contraseña debe tener al menos 6 caracteres.";
    }
    // Validación básica de formato de email
    if (!email.contains("@") || !email.contains(".")) {
      return "ERROR:El email no es válido. Debe contener @ y un punto.";
    }
    // Validación de unicidad del username
    if (Dao.existUsername(username.trim())) {
      return "ERROR:El username '" + username.trim() + "' ya está en uso.";
    }
    // Hashear contraseña antes de guardar
    String hashedPassword = hashPassword(password);
    if (hashedPassword == null) {
      return "ERROR:Error interno al encriptar la contraseña.";
    }

    // Crear y guardar el usuario
    User newUser = new User(username.trim(), hashedPassword, email.trim(), true);
    boolean ok = Dao.insertNewUser(newUser);

    return ok
        ? "OK:Usuario '" + username.trim() + "' registrado exitosamente." // Si no es null retorna este mensaje
        : "ERROR:No se pudo guardar el usuario en la base de datos."; // Si es null retorna este mensaje
  }

  // Retorna todos los usuarios registrados en el sistema
  public List<User> listUsers() {
    return Dao.listAllUsers();
  }

  /** Busca un usuario por username. Retorna null si no existe. */
  public User searchByUsername(String username) {
    return Dao.searchByUsername(username);
  }

  /** Busca un usuario por ID. Retorna null si no existe. */
  public User searchById(int id) {
    return Dao.searchById(id);
  }

  // Cambia el username de un usuario.
  public String changeUsername(int id, String newUsername) {
    if (newUsername == null || newUsername.trim().isEmpty())
      return "ERROR:El nuevo username no puede estar vacío.";
    if (Dao.existUsername(newUsername.trim()))
      return "ERROR:El username '" + newUsername.trim() + "' ya está en uso.";

    boolean ok = Dao.updateUsername(id, newUsername.trim());
    return ok
        ? "OK:Username actualizado correctamente a '" + newUsername.trim() + "'."
        : "ERROR:No se pudo actualizar el username.";
  }

  // Cambia la contraseña de un usuario
  public String changePassword(int id, String nuevaPassword) {
    if (nuevaPassword == null || nuevaPassword.trim().isEmpty())
      return "ERROR:La nueva contraseña no puede estar vacía.";
    if (nuevaPassword.length() < 6)
      return "ERROR:La contraseña debe tener al menos 6 caracteres.";

    String hash = hashPassword(nuevaPassword);
    if (hash == null)
      return "ERROR:Error al encriptar la contraseña.";

    boolean ok = Dao.updatePassword(id, hash);
    return ok ? "OK:Contraseña actualizada correctamente." : "ERROR:No se pudo actualizar la contraseña.";
  }

  // Alterna el estado activo/inactivo de un usuario
  public String changeStatus(int id, boolean actualStatus) {
    boolean newStatus = !actualStatus;
    boolean ok = Dao.changeStatus(id, newStatus);
    String text = newStatus ? "ACTIVO" : "INACTIVO";
    return ok
        ? "OK:El usuario ahora está " + text + "."
        : "ERROR:No se pudo cambiar el estado del usuario.";
  }

  // Elimina un usuario permanentemente por su ID
  public String deleteUser(int id) {
    boolean ok = Dao.deleteUser(id);
    return ok
        ? "OK:Usuario eliminado correctamente."
        : "ERROR:No se pudo eliminar el usuario (ID: " + id + ").";
  }
}