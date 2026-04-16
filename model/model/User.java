package model;


 //Clase que representa a un usuario del sistema.
public class User {

  private int    id;
  private String username;
  private String password;
  private String email;
  private boolean isActive;

  
  public User() {
  }

  public User(int id, String username, String password, String email, boolean isActive) {
    this.id          = id;
    this.username    = username;
    this.password    = password;
    this.email       = email;
    this.isActive    = isActive;
  }
 
  public User(String username, String password, String email, boolean isActive) {
        this.username = username;
        this.password = password;
        this.email    = email;
        this.isActive = isActive;
    }

public int     getId()       { return id; }
    public String  getUsername() { return username; }
    public String  getPassword() { return password; }
    public String  getEmail()    { return email; }
    public boolean isActive()    { return isActive; }

    // ── Setters (acceso de escritura) ────────────────────────
    public void setId(int id)             { this.id = id; }
    public void setUsername(String u)     { this.username = u; }
    public void setPassword(String p)     { this.password = p; }
    public void setEmail(String e)        { this.email = e; }
    public void setActive(boolean active) { this.isActive = active; }

    @Override
    public String toString() {
        return "[" + id + "] " + username + " — " + email +
               " — " + (isActive ? "✅ Activo" : "❌ Inactivo");
    }
}